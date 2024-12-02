
# ING Case Study - Credit Module API

This project is a backend Credit Module API designed for managing loans in a bank system. Employees can create loans, list loans for customers, manage loan installments, and handle payments. The project uses **Spring Boot**, **Spring Security**, and **Swagger** for API documentation and testing.

## Features

- **Loan Management:**
  - Create loans with specific validation rules.
  - List loans for specific customers.
  - Handle loan installment payments.

- **Installment Management:**
  - Track due dates.
  - Manage early payments with rewards.
  - Add penalties for overdue installments.

- **Authentication and Authorization:**
  - Admin users can manage all customers.
  - Customer users can only view and manage their own loans.

- **Profiles:**
  - **Dev Environment**:
    - Swagger UI is active.
    - `GlobalExceptionHandler` is disabled.
    - Pre-initialized admin and customer users for testing.
  - **Prod Environment**:
    - Swagger UI is disabled.
    - `GlobalExceptionHandler` is active.

## API Endpoints

| Endpoint                      | Method | Description                        | Authentication  |
|-------------------------------|--------|------------------------------------|-----------------|
| `/auth/register`              | POST   | Register a new user.               | Public          |
| `/auth/authenticate`          | POST   | Authenticate user and get a token. | Public          |
| `/loan/create`                | POST   | Create a loan for a customer.      | Admin, Customer |
| `/loan/pay`                   | POST   | Pay loans for a customer.          | Admin, Customer |
| `/loan`           | GET    | List loans by customer id          | Admin, Customer |
| `/loan-installment`           | GET    | List installments for a loan.      | Admin, Customer |


## Initial Data for Dev Environment

The following users are pre-initialized in the `dev` environment for testing purposes:

1. **Admin User:**
   - **Email:** `emirhan.lekesiztas@gmail.com`
   - **Password:** `test1234`
   - **Role:** Admin

2. **Customer User:**
   - **Email:** `ahmet.aktan@gmail.com`
   - **Password:** `test1234`
   - **Role:** Customer

These users can be used to authenticate via `/auth/authenticate` and test secured endpoints.

## Swagger Documentation

- **URL:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) (Active only in `dev` environment).
- Use the **Admin** or **Customer** credentials to authenticate and test secured endpoints.

## Running the Application

### Prerequisites

- Java 17 or above.
- Maven 3.8+.

### Build and Run

1. Clone the repository:
   ```bash
   git clone https://github.com/emirlekesiztas/credit-module-api.git
   cd ing-case-study
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

4. For production:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```

### H2 Database Access

The application uses an in-memory H2 database in `dev` mode. To access the H2 console:

- **URL:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** *password*

## Unit Tests

Run all tests using:
```bash
mvn test
```

## Notes

- The application supports different configurations for `dev` and `prod` environments.
- `GlobalExceptionHandler` is enabled only in `prod` to provide standardized error responses.
- Swagger is disabled in `prod` for security reasons.
