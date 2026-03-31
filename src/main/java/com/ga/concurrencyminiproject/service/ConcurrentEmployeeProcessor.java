package com.ga.concurrencyminiproject.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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
		Semaphore semaphore = new Semaphore(poolSize);
		ReentrantLock lock = new ReentrantLock();
		try {
			List<SalaryChange> result = new ArrayList<>(employees.size());
			List<Future<?>> futures = new ArrayList<>(employees.size());

			for (Employee employee : employees) {
				futures.add(executor.submit(() -> {
					try {
						semaphore.acquire();
						double raisePercent = salaryRules.calculateRaisePercent(employee, today);
						double salaryAfter = salaryRules.calculateNewSalary(employee, today);

						lock.lock();
						try {
							result.add(new SalaryChange(
									employee.getId(),
									employee.getName(),
									employee.getSalary(),
									raisePercent,
									salaryAfter
							));
						} finally {
							lock.unlock();
						}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						throw new RuntimeException("Worker interrupted", e);
					} finally {
						semaphore.release();
					}
				}));
			}

			for (Future<?> future : futures) {
				try {
					future.get();
				} catch (ExecutionException e) {
					throw new RuntimeException("Failed to process employee", e.getCause());
				}
			}

			result.sort(Comparator.comparingLong(SalaryChange::id));
			return result;
		} finally {
			executor.shutdown();
			executor.awaitTermination(10, TimeUnit.SECONDS);
		}
	}
}

