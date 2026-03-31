package com.ga.concurrencyminiproject.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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

	public ProcessController(EmployeeCsvReader employeeCsvReader, ConcurrentEmployeeProcessor concurrentEmployeeProcessor) {
		this.employeeCsvReader = employeeCsvReader;
		this.concurrentEmployeeProcessor = concurrentEmployeeProcessor;
	}

	@PostMapping("/process")
	public List<SalaryChange> process() throws IOException {
		LocalDate today = LocalDate.now();
		List<Employee> employees = employeeCsvReader.readAll();

		try {
			return concurrentEmployeeProcessor.processConcurrently(employees, today, 4);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Processing was interrupted", e);
		}
	}
}

