package com.example.batchjpapagingitemreader.job;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.example.batchjpapagingitemreader.dto.EmployeeDto;

@Component
public class EmployeeCsvRowMapper implements RowMapper<EmployeeDto> {

	@Override
	public EmployeeDto mapRow(ResultSet rs, int i) throws SQLException {
		EmployeeDto dto = new EmployeeDto();
		dto.setId(rs.getInt("id"));
		dto.setName(rs.getString("name"));
		dto.setAge(rs.getInt("age"));
		//dto.setLastModifiedDate(rs.getDate("last_modified_date;"));
		return dto;
	}

}
