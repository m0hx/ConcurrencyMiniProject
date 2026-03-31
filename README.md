# Concurrent CSV Data Processor

A Java Spring Boot mini project that reads employee data from a CSV file and applies salary increases using a thread pool with concurrency control (semaphore + lock).

## What it does (quick)

- Reads employees from a CSV file
- Calculates each employee’s salary raise using role, joined date, and completed projects %
- Processes employees concurrently using a thread pool
- Uses a semaphore + lock to keep concurrent processing thread-safe

## Requirements

- IDE: **IntelliJ IDEA 2026.1** (recommended)
- Java SDK: **17.0.17**
- Build tool: **Maven**
- API testing: **Postman** (recommended)

## Project setup (Spring Initializr)

- Project: **Maven**
- Language: **Java**
- Spring Boot: **4.0.5**
- Packaging: **Jar**
- Java: **17.0.17**
- Dependencies: **Spring Web**, **Lombok**

## Run

Run the `ConcurrencyminiprojectApplication` class from IntelliJ IDEA.

The app starts on `http://localhost:8080`.

## Test the API

Use Postman.

### 📌 API Endpoints

| Request Type | URL            | Functionality                                                                       | Access |
| ------------ | -------------- | ----------------------------------------------------------------------------------- | ------ |
| POST         | `/api/process` | Read employees from CSV, apply salary raise rules, and return before/after salaries | Public |

Response is a JSON array containing `salaryBefore`, `raisePercent`, and `salaryAfter` for each employee.

### How to use in Postman

1. Set method to **POST**
2. URL: `http://localhost:8080/api/process`
3. Body: **none**
4. Click **Send**

### Concurrency

- Uses a fixed thread pool (`ExecutorService`) to process employees in parallel.
- Uses a `Semaphore` to limit how many tasks run the work section at the same time.
- Uses a `ReentrantLock` to protect the shared results list while multiple threads add items.

## CSV input

Default CSV is loaded from the classpath:

- `src/main/resources/test_employees.csv`

Configured in:

- `src/main/resources/application.properties` (`app.csv.path`)
