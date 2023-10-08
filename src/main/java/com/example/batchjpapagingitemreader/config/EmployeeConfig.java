package com.example.batchjpapagingitemreader.config;

import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobParameterExecutionContextCopyListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.batchjpapagingitemreader.dto.EmployeeDto;
import com.example.batchjpapagingitemreader.job.EmployeeCsvProcessor;
import com.example.batchjpapagingitemreader.job.EmployeeCsvReader;
import com.example.batchjpapagingitemreader.job.EmployeeCsvWriter;

@Configuration
public class EmployeeConfig {

	@Value("${chunk.size}")
	private int chunkSize;

	@Bean
	@StepScope
	EmployeeCsvReader employeeCsvItemReader(DataSource dataSource,
			@Value("#{stepExecutionContext[fromDate]}") Date fromDate,
			@Value("#{stepExecutionContext[toDate]}") Date toDate,
			@Value("#{stepExecutionContext[pageSize]}") long pageSize) {
		return new EmployeeCsvReader(dataSource, fromDate, toDate, pageSize);
	}

	@Bean
	ItemWriter<Map<String, Object>> employeeCsvItemWriter() {
		return new EmployeeCsvWriter();
	}

	@Bean
	ItemProcessor<EmployeeDto, Map<String, Object>> employeeCsvItemProcessor() {
		return new EmployeeCsvProcessor();
	}

	/**
	 * Creates a bean that represents the only step of our batch job.
	 * 
	 * @param reader
	 * @param writer
	 * @param stepBuilderFactory
	 * @return
	 */
	@Bean
	Step employeeCsvStep(@Qualifier("employeeCsvItemReader") ItemReader<EmployeeDto> reader,
			@Qualifier("employeeCsvItemProcessor") ItemProcessor<EmployeeDto, Map<String, Object>> processor,
			@Qualifier("employeeCsvItemWriter") ItemWriter<Map<String, Object>> writer, JobRepository jobRepository,
			PlatformTransactionManager txManager) {
		return new StepBuilder("employeeCsvStep", jobRepository)
				.<EmployeeDto, Map<String, Object>>chunk(chunkSize, txManager).reader(reader).processor(processor)
				.writer(writer).listener(new JobParameterExecutionContextCopyListener()).build();
	}

	/**
	 * Creates a bean that represents our example batch job.
	 * 
	 * @param exampleJobStep
	 * @param jobBuilderFactory
	 * @return
	 */
	@Bean
	Job employeeCsvJob(@Qualifier("employeeCsvStep") Step employeeCsvJobStep, JobRepository jobRepository) {
		return new JobBuilder("employeeCsvJob", jobRepository).incrementer(new RunIdIncrementer())
				.flow(employeeCsvJobStep).end().build();
	}
}
