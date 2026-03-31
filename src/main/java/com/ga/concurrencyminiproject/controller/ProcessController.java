package com.ga.concurrencyminiproject.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ga.concurrencyminiproject.io.EmployeeCsvReader;
import com.ga.concurrencyminiproject.model.Employee;
import com.ga.concurrencyminiproject.service.ConcurrentEmployeeProcessor;

@RestController
@RequestMapping("/api")
public class ProcessController {
	private final EmployeeCsvReader employeeCsvReader;
	private final ConcurrentEmployeeProcessor concurrentEmployeeProcessor;
	private final int poolSize;

	public ProcessController(
			EmployeeCsvReader employeeCsvReader,
			ConcurrentEmployeeProcessor concurrentEmployeeProcessor,
			@Value("${app.pool.size:4}") int poolSize
	) {
		this.employeeCsvReader = employeeCsvReader;
		this.concurrentEmployeeProcessor = concurrentEmployeeProcessor;
		this.poolSize = poolSize;
	}

	@PostMapping("/process")
	public List<SalaryChange> process() throws IOException {
		LocalDate today = LocalDate.now();
		List<Employee> employees = employeeCsvReader.readAll();

		try {
			return concurrentEmployeeProcessor.processConcurrently(employees, today, poolSize);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Processing was interrupted", e);
		}
	}
}

