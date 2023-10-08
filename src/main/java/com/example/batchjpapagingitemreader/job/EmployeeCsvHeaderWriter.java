package com.example.batchjpapagingitemreader.job;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

public class EmployeeCsvHeaderWriter implements FlatFileHeaderCallback {

	private final String header;

	EmployeeCsvHeaderWriter(String header) {
		this.header = header;
	}

	@Override
	public void writeHeader(Writer writer) throws IOException {
		writer.write(header);
	}

}
