package com.example.batchjpapagingitemreader.controller;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.batchjpapagingitemreader.dto.EmployeeDto;
import com.example.batchjpapagingitemreader.entity.Employee;
import com.example.batchjpapagingitemreader.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/employee")
@Slf4j
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	@Qualifier("employeeCsvJob")
	Job employeeCsvJob;

	@Value("${page.size}")
	private long pageSize;
	
	@PostMapping
	public Employee saveEmployee(@RequestBody Employee employee) {
		employee.setLastModifiedDate(new Date());
		return employeeService.save(employee);
	}

	@PostMapping("/generateCsv")
	public String generateReport(@RequestBody EmployeeDto employeeDto) {
		try {
			String header = employeeService.getHeader();
			JobParameters jobParameters = new JobParametersBuilder()
					.addString("filePath", "D:\\temp")
					.addString("csvHeader", header)
					.addLong("pageSize", pageSize)
					.addDate("date", new Date())
					.addDate("fromDate", employeeDto.getFromDate())
					.addDate("toDate", employeeDto.getToDate()).toJobParameters();
			JobExecution execution = jobLauncher.run(employeeCsvJob, jobParameters);
			log.info("Job Execution Status ... {}", execution.getStatus().name());
			if ("COMPLETED".equalsIgnoreCase(execution.getStatus().name())) {
				return "Batch Job Sucess";
			}
		}catch(Exception ex){
			log.error("Exception ::", ex);
		}
		return "Batch Job Failed";
	}
}
