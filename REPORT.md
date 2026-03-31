# Report — Concurrent CSV Data Processor

## Summary

This project is an application that reads employee records from a CSV file and applies salary increases. The processing is done concurrently using a fixed-size thread pool, while keeping shared data safe using concurrency control.

## What was implemented

- **CSV reading**: Employee data is read from `src/main/resources/test_employees.csv`.
- **Salary rules**:
  - If `projectCompletion < 0.60` → no salary increase
  - Joined date → **+2%** per full year worked (only if at least 1 full year)
  - Role-based increase:
    - Director: **+5%**
    - Manager: **+2%**
    - Employee: **+1%**
- **API**:
  - `POST /api/process` reads the CSV, processes employees, and returns a JSON array showing:
    - `salaryBefore`, `raisePercent`, `salaryAfter`

## Concurrency approach

- **Thread pooling**: Uses `ExecutorService` with `Executors.newFixedThreadPool(...)` to process employees in parallel.
- **Semaphore**: Limits how many tasks can do the “work section” concurrently.
- **ReentrantLock**: Protects the shared result list while multiple threads add items.

## Testing / outcomes

- Ran the application on **Java 17.0.17**.
- Verified `POST http://localhost:8080/api/process` returns a list of 30 employees from the sample CSV.
- Verified employees with completion below 60% receive **0%** raise.
- Verified results are returned sorted by employee `id`.
