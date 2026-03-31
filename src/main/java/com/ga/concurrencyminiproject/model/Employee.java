package com.ga.concurrencyminiproject.model;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Employee {
	long id;
	String name;
	double salary;
	LocalDate joinedDate;
	Role role;
	double projectCompletion;
}

