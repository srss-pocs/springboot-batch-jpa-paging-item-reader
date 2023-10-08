package com.example.batchjpapagingitemreader.dto;

import java.util.Date;

import lombok.Data;

@Data
public class EmployeeDto {

	private long id;
	private String name;
	private int age;
	private Date lastModifiedDate;
	private Date fromDate;
	private Date toDate;
	
}
