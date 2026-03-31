package com.ga.concurrencyminiproject.service;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Service;

import com.ga.concurrencyminiproject.model.Employee;

@Service
public class SalaryRules {
	public double calculateRaisePercent(Employee employee, LocalDate today) {
		if (employee.getProjectCompletion() < 0.60) {
			return 0.0;
		}

		double rolePercent = switch (employee.getRole()) {
			case DIRECTOR -> 0.05;
			case MANAGER -> 0.02;
			case EMPLOYEE -> 0.01;
		};

		if (today.isBefore(employee.getJoinedDate())) {
			return rolePercent;
		}

		int fullYears = Period.between(employee.getJoinedDate(), today).getYears();
		double yearsPercent = fullYears >= 1 ? (fullYears * 0.02) : 0.0;

		return rolePercent + yearsPercent;
	}

	public double calculateNewSalary(Employee employee, LocalDate today) {
		double raisePercent = calculateRaisePercent(employee, today);
		return employee.getSalary() * (1.0 + raisePercent);
	}
}

