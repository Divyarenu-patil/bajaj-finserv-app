# Bajaj Finserv Health - Qualifier 1 - JAVA

## Overview
This Spring Boot application implements the requirements for Bajaj Finserv Health Qualifier 1:

1. On startup, sends a POST request to generate a webhook
2. Solves the SQL problem for calculating younger employees count by department
3. Submits the solution to the webhook URL using JWT authentication

## SQL Problem Solution
The application solves the problem of calculating the number of employees who are younger than each employee, grouped by their respective departments.

**SQL Query:**
```sql
SELECT 
    e1.EMP_ID,
    e1.FIRST_NAME, 
    e1.LAST_NAME,
    d.DEPARTMENT_NAME,
    COUNT(e2.EMP_ID) as YOUNGER_EMPLOYEES_COUNT
FROM EMPLOYEE e1
JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID
LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT AND e2.DOB > e1.DOB
GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME
ORDER BY e1.EMP_ID DESC
```

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Using the JAR file
```bash
java -jar target/bajaj-finserv-app-1.0.0.jar
```

### Using Maven
```bash
mvn clean package
mvn spring-boot:run
```

## Application Flow
1. Application starts up
2. WebhookService (CommandLineRunner) executes automatically
3. Sends POST request to generate webhook with sample data
4. Receives webhook URL and access token
5. Submits the SQL solution to the webhook URL
6. Application completes and can be terminated

## Architecture
- **BajajFinservApplication**: Main Spring Boot application class
- **WebhookService**: Handles the webhook generation and solution submission
- **DTOs**: Data transfer objects for API requests and responses
  - WebhookRequestDTO
  - WebhookResponseDTO  
  - SolutionRequestDTO

## Dependencies
- Spring Boot Web Starter
- Spring Boot WebFlux (for RestTemplate)
- Jackson for JSON processing
