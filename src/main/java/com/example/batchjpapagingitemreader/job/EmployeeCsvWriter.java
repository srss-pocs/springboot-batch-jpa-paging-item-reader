package com.example.batchjpapagingitemreader.job;

import java.util.Map;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.PassThroughFieldExtractor;
import org.springframework.core.io.FileSystemResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmployeeCsvWriter implements ItemWriter<Map<String, Object>> {

	private final FlatFileItemWriter<Map<String, Object>> writer;
	String filePath = null;
	String csvHeader = null;
	
	EmployeeCsvHeaderWriter csvHeaderWriter = null;
	

	public EmployeeCsvWriter() {
	writer = new FlatFileItemWriter<>();
	new FileSystemResource("");
	}

	@BeforeStep
	public void beforeStep(final StepExecution stepExecution) {
		JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
		filePath = parameters.getString("filePath");
		csvHeader = parameters.getString("csvHeader");
	}

	@Override
	public void write(Chunk<? extends Map<String, Object>> list) throws Exception {
		log.info("Filepath--->>{}", filePath);
		writer.setResource(new FileSystemResource(filePath));
		csvHeaderWriter = new EmployeeCsvHeaderWriter(csvHeader);
		writer.setHeaderCallback(csvHeaderWriter);
		writer.setLineAggregator(getDelimitedLineAggregator(list));
		writer.afterPropertiesSet();
		writer.open(new ExecutionContext());
		writer.setAppendAllowed(true);
		writer.write(list);
	}

	@AfterWrite
	private void close() {
		this.writer.close();
	}

	public DelimitedLineAggregator<Map<String, Object>> getDelimitedLineAggregator(
			Chunk<? extends Map<String, Object>> list) {
		PassThroughFieldExtractor<Map<String, Object>> fieldExtractor = new PassThroughFieldExtractor<>();
		for (Map<String, Object> item : list) {
			fieldExtractor.extract(item);
		}
		DelimitedLineAggregator<Map<String, Object>> delimitedLineAggregator = new DelimitedLineAggregator<>();
		delimitedLineAggregator.setDelimiter(",");
		delimitedLineAggregator.setFieldExtractor(fieldExtractor);
		return delimitedLineAggregator;
	}

}
