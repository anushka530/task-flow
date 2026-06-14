# TaskFlow Backend

A production-style project management backend built with Spring Boot.

## About
REST API backend for managing projects and tasks with secure authentication,
role-based access control, and clean layered architecture.

## Features
- JWT authentication with refresh token rotation
- Role-based access control (Admin / User) using Spring Security
- DTO pattern to isolate data transfer logic
- Global exception handling with structured error responses
- @Transactional-safe database operations
- High test coverage using JUnit 5 and Mockito

## Tech Stack
Java · Spring Boot · Spring Security · JWT · JPA · Hibernate · MySQL · JUnit 5 · Mockito · Maven

## Architecture
Controller → Service → Repository (strict layered architecture)

## How to Run
1. Clone the repo
   git clone https://github.com/your-username/taskflow-backend.git

2. Configure database in application.properties
   spring.datasource.url=jdbc:mysql://localhost:3306/taskflow
   spring.datasource.username=your_username
   spring.datasource.password=your_password

3. Run the application
   mvn spring-boot:run

4. API runs on http://localhost:8080

## API Endpoints
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | /api/auth/register | Register new user | No |
| POST | /api/auth/login | Login + get JWT | No |
| POST | /api/auth/refresh | Refresh access token | No |
| GET | /api/projects | Get all projects | Yes |
| POST | /api/projects | Create project | Admin |
| PUT | /api/projects/{id} | Update project | Admin |
| DELETE | /api/projects/{id} | Delete project | Admin |
| GET | /api/tasks | Get all tasks | Yes |
| POST | /api/tasks | Create task | Yes |
