package com.ga.concurrencyminiproject.model;

public enum Role {
	DIRECTOR,
	MANAGER,
	EMPLOYEE;

	public static Role fromCsv(String value) {
		if (value == null) {
			throw new IllegalArgumentException("role is required");
		}
		return switch (value.trim().toLowerCase()) {
			case "director" -> DIRECTOR;
			case "manager" -> MANAGER;
			case "employee" -> EMPLOYEE;
			default -> throw new IllegalArgumentException("Unknown role: " + value);
		};
	}
}
