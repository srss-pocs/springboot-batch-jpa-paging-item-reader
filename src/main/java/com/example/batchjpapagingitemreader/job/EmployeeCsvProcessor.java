package com.example.batchjpapagingitemreader.job;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;

import com.example.batchjpapagingitemreader.dto.EmployeeDto;

public class EmployeeCsvProcessor implements ItemProcessor<EmployeeDto, Map<String, Object>> {

	@Override
	public Map<String, Object> process(EmployeeDto item) throws Exception {
		Map<String, Object> csvMap = new LinkedHashMap<>();
		csvMap.put("id", item.getId());
		csvMap.put("name", item.getName());
		csvMap.put("age", item.getAge());
		csvMap.put("lastModifiedDate", item.getLastModifiedDate());
		return csvMap;
	}
}
