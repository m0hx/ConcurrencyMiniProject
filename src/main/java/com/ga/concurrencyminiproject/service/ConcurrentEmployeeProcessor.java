package com.ga.concurrencyminiproject.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import com.ga.concurrencyminiproject.controller.SalaryChange;
import com.ga.concurrencyminiproject.model.Employee;

@Service
public class ConcurrentEmployeeProcessor {
	private final SalaryRules salaryRules;

	public ConcurrentEmployeeProcessor(SalaryRules salaryRules) {
		this.salaryRules = salaryRules;
	}

	public List<SalaryChange> processConcurrently(List<Employee> employees, LocalDate today, int poolSize)
			throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(poolSize);
		try {
			List<Future<SalaryChange>> futures = new ArrayList<>(employees.size());

			for (Employee employee : employees) {
				Callable<SalaryChange> task = () -> {
					double raisePercent = salaryRules.calculateRaisePercent(employee, today);
					double salaryAfter = salaryRules.calculateNewSalary(employee, today);
					return new SalaryChange(
							employee.getId(),
							employee.getName(),
							employee.getSalary(),
							raisePercent,
							salaryAfter
					);
				};
				futures.add(executor.submit(task));
			}

			List<SalaryChange> result = new ArrayList<>(employees.size());
			for (Future<SalaryChange> future : futures) {
				try {
					result.add(future.get());
				} catch (ExecutionException e) {
					throw new RuntimeException("Failed to process employee", e.getCause());
				}
			}

			result.sort(Comparator.comparingLong(SalaryChange::id));
			return result;
		} finally {
			executor.shutdown();
		}
	}
}

