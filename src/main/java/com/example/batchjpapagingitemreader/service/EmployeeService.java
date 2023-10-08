package com.example.batchjpapagingitemreader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.batchjpapagingitemreader.entity.Employee;
import com.example.batchjpapagingitemreader.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;
	
	public String getHeader() {
		StringBuilder headerBuilder = new StringBuilder();
		headerBuilder.append("ID").append(",");
		headerBuilder.append("Name").append(",");
		headerBuilder.append("Age").append(",");
		headerBuilder.append("Date/Time");
		return headerBuilder.toString();
	}
	
	public Employee save(Employee employee) {
		return employeeRepository.save(employee);
	}
}
