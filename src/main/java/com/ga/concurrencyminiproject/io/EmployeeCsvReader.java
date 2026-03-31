package com.ga.concurrencyminiproject.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.ga.concurrencyminiproject.model.Employee;
import com.ga.concurrencyminiproject.model.Role;

@Component
public class EmployeeCsvReader {
	private final ResourceLoader resourceLoader;
	private final String csvPath;

	public EmployeeCsvReader(ResourceLoader resourceLoader, @Value("${app.csv.path}") String csvPath) {
		this.resourceLoader = resourceLoader;
		this.csvPath = csvPath;
	}

	public List<Employee> readAll() throws IOException {
		Resource resource = resourceLoader.getResource(csvPath);
		if (!resource.exists()) {
			throw new IOException("CSV not found: " + csvPath);
		}

		List<Employee> employees = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty()) {
					continue;
				}
				String[] parts = line.split(",");
				if (parts.length != 6) {
					throw new IOException("Invalid CSV line (expected 6 columns): " + line);
				}

				long id = Long.parseLong(parts[0].trim());
				String name = parts[1].trim();
				double salary = Double.parseDouble(parts[2].trim());
				LocalDate joinedDate = LocalDate.parse(parts[3].trim());
				Role role = Role.fromCsv(parts[4].trim());
				double projectCompletion = Double.parseDouble(parts[5].trim());

				employees.add(Employee.builder()
						.id(id)
						.name(name)
						.salary(salary)
						.joinedDate(joinedDate)
						.role(role)
						.projectCompletion(projectCompletion)
						.build());
			}
		}

		return employees;
	}
}

