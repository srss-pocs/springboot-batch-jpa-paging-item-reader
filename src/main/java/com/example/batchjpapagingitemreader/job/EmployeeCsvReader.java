package com.example.batchjpapagingitemreader.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;

import com.example.batchjpapagingitemreader.dto.EmployeeDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmployeeCsvReader extends JdbcPagingItemReader<EmployeeDto> {

	String fromDate = null;
	String toDate = null;

	public EmployeeCsvReader(DataSource ds, @Value("#{jobParameters['fromDate']}") Date fromDate,
			@Value("#{jobParameters['toDate']}") Date toDate, Long pageSize) {
		super();
		setDataSource(ds);
		setPageSize(pageSize.intValue());
		setRowMapper(new EmployeeCsvRowMapper());
		Map<String, Object> parameters = new HashMap<>();
		if (fromDate != null) {
			parameters.put("fromDate", fromDate);
			parameters.put("toDate", toDate);
		}
		//setParameterValues(parameters);
		setQueryProvider(queryProvider(ds, fromDate, toDate));
	}

	private PagingQueryProvider queryProvider(DataSource ds, Date fromDate, Date toDate) {
		SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
		queryProvider.setDataSource(ds);
		queryProvider.setSelectClause("SELECT id,name,age,last_modified_date");
		queryProvider.setFromClause("FROM public.employee");
		queryProvider.setWhereClause(getWhereClause(fromDate, toDate));
		queryProvider.setSortKey("last_modified_date");
		try {
			return queryProvider.getObject();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}

	private String getWhereClause(Date fromDate, Date toDate) {
		log.info("fromDate-- {}", fromDate);
		log.info("ToDate-- {}", toDate);
	/*	StringBuilder queryBuilder = new StringBuilder(" WHERE ");
		queryBuilder.append("last_modified_date >= :fromDate AND last_modified_date <= :toDate");
		return queryBuilder.toString(); */
		return "";
	}

}
