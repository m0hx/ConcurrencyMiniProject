package com.ga.concurrencyminiproject.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ga.concurrencyminiproject.io.EmployeeCsvReader;
import com.ga.concurrencyminiproject.model.Employee;
import com.ga.concurrencyminiproject.service.SalaryRules;

@RestController
@RequestMapping("/api")
public class ProcessController {
	private final EmployeeCsvReader employeeCsvReader;
	private final SalaryRules salaryRules;

	public ProcessController(EmployeeCsvReader employeeCsvReader, SalaryRules salaryRules) {
		this.employeeCsvReader = employeeCsvReader;
		this.salaryRules = salaryRules;
	}

	@PostMapping("/process")
	public List<SalaryChange> process() throws IOException {
		LocalDate today = LocalDate.now();
		List<Employee> employees = employeeCsvReader.readAll();

		List<SalaryChange> result = new ArrayList<>(employees.size());
		for (Employee employee : employees) {
			double raisePercent = salaryRules.calculateRaisePercent(employee, today);
			double salaryAfter = salaryRules.calculateNewSalary(employee, today);
			result.add(new SalaryChange(
					employee.getId(),
					employee.getName(),
					employee.getSalary(),
					raisePercent,
					salaryAfter
			));
		}
		return result;
	}
}

