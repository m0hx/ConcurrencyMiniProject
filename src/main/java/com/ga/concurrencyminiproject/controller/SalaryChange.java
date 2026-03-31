package com.ga.concurrencyminiproject.controller;

public record SalaryChange(
		long id,
		String name,
		double salaryBefore,
		double raisePercent,
		double salaryAfter
) {
}

