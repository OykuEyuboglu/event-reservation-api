# 🎟️ Spring Boot Event Reservation REST API
 
<div align="center">
 
*A secure and scalable Event Reservation REST API built with Spring Boot, PostgreSQL, and Spring Security. The project demonstrates JWT authentication, role-based authorization, reservation management, optimistic locking for concurrency control, scheduled tasks, and comprehensive testing.*
 
![Java](https://img.shields.io/badge/Java-23-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity)
![JWT](https://img.shields.io/badge/JWT-Authentication-black?style=for-the-badge&logo=jsonwebtokens)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5)
![Mockito](https://img.shields.io/badge/Mockito-78A641?style=for-the-badge)
 
</div>

---
 
# 📌 Project Overview
 
Spring Boot Event Reservation API is a RESTful backend application developed for managing events and seat reservations securely and efficiently.
 
The project follows a layered architecture and demonstrates modern backend development concepts including JWT authentication, role-based authorization, DTO mapping, validation, global exception handling, scheduled tasks, optimistic locking, and comprehensive testing.
 
Unlike the original internship project specification, which suggested Java 17 and MongoDB, this implementation uses **Java 23** and **PostgreSQL** together with **Spring Data JPA** and **Hibernate** to provide a relational database solution.
 
The application allows administrators to manage events while authenticated users can reserve, confirm, and cancel reservations. To ensure data consistency, the system automatically expires inactive reservations and prevents concurrent seat bookings using JPA Optimistic Locking.
 
---
 
# 🚀 Features
 
## 🔐 Authentication & Authorization
 
- User registration
- User login with JWT
- Password encryption
- Stateless authentication
- Role-based authorization (ADMIN / USER)
- Protected REST endpoints
---
 
## 🎫 Event Management
 
- Create events
- Update events
- Delete events
- Retrieve all events
- Retrieve event details
- Automatic seat generation during event creation
- Retrieve seats for an event
---
 
## 🎟 Reservation Management
 
- Create reservations
- Confirm reservations
- Cancel reservations
- Retrieve reservation details
- Retrieve the authenticated user's reservations
- Automatic reservation expiration
- Automatic seat status management
---
 
## ⚡ Concurrency Control
 
- Prevents double booking using JPA Optimistic Locking (`@Version`)
---
 
## 🧪 Testing
 
- Service layer unit tests using JUnit 5 and Mockito
- Security integration tests using MockMvc
---
 
# 📑 API Documentation
 
Swagger UI is integrated into the project for interactive API documentation.
 
After running the application, the documentation is available at:
 
```
http://localhost:8080/swagger-ui/index.html
```
 
Using Swagger UI, you can:
 
- View all available REST endpoints
- Inspect request and response models
- Test endpoints directly from the browser
- Authenticate using JWT for protected endpoints
---
 
# 📷 Swagger Preview

### Swagger Endpoint List

<img src="images/swagger-home.png" width="40%" alt="Swagger UI Preview"/>

```
 
---
 
# 🛠 Technology Stack
 
## Backend
 
```text
Java 23
Spring Boot
Spring MVC
Spring Data JPA
Hibernate
Spring Security
RESTful API
Jakarta Bean Validation
```
 
## Database
 
```text
PostgreSQL
pgAdmin
```
 
## Authentication
 
```text
JWT (JSON Web Token)
Spring Security
BCrypt Password Encoder
```
 
## Documentation
 
```text
Swagger / OpenAPI
```
 
## Testing
 
```text
JUnit 5
Mockito
Spring Boot Test
MockMvc
```
 
## Utilities
 
```text
MapStruct
Lombok
Maven
SLF4J Logging
```
 
---
 
# 🏗 Project Architecture
 
The project follows a layered architecture to ensure separation of concerns, maintainability, and scalability.
 
```text
Controller
      │
      ▼
Service
      │
      ▼
Repository
      │
      ▼
PostgreSQL Database
```
 
Each layer has a single responsibility:
 
- **Controller** → Handles HTTP requests and responses.
- **Service** → Contains business logic.
- **Repository** → Performs database operations.
- **Entity** → Represents database tables.
- **DTO** → Transfers data between layers.
- **Mapper** → Converts Entities and DTOs using MapStruct.
---
 
# 📂 Project Structure
 
```text
src
└── main
    ├── java
    │   └── com.oyku.event_reservation_api
    │       ├── config
    │       ├── controller
    │       ├── dto
    │       ├── entity
    │       ├── enums
    │       ├── exception
    │       ├── mapper
    │       ├── repository
    │       ├── security
    │       │     ├── jwt
    │       │     └── config
    │       ├── service
    │       │     └── impl
    │       ├── scheduler
    │       ├── validation
    │       └── EventReservationApiApplication.java
    │
    └── resources
          └── application.properties
```
 
---
 
# 🔄 Request Flow
 
```text
Client
 
    │
 
    ▼
 
Controller
 
    │
 
    ▼
 
Service
 
    │
 
    ▼
 
Repository
 
    │
 
    ▼
 
PostgreSQL Database
 
    │
 
    ▼
 
Response DTO
```
 
---
 
# 📦 API Endpoints
 
## 🔐 Authentication
 
| Method | Endpoint | Description |
| ------ | -------- | ----------- |
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Authenticate user and generate JWT |
 
---
 
## 🎫 Events
 
| Method | Endpoint | Description | Access |
| ------ | -------- | ----------- | ------ |
| POST | `/api/events` | Create a new event | ADMIN |
| GET | `/api/events` | Retrieve all events | USER / ADMIN |
| GET | `/api/events/{id}` | Retrieve event details | USER / ADMIN |
| PUT | `/api/events/{id}` | Update an event | ADMIN |
| DELETE | `/api/events/{id}` | Delete an event | ADMIN |
| GET | `/api/events/{id}/seats` | Retrieve seats for an event | USER / ADMIN |
 
---
 
## 🎟 Reservations
 
| Method | Endpoint | Description | Access |
| ------ | -------- | ----------- | ------ |
| POST | `/api/reservations` | Create a reservation | USER |
| GET | `/api/reservations/{id}` | Retrieve reservation details | USER |
| GET | `/api/reservations/my` | Retrieve the authenticated user's reservations | USER |
| PATCH | `/api/reservations/{id}/confirm` | Confirm reservation | USER |
| PATCH | `/api/reservations/{id}/cancel` | Cancel reservation | USER |
 
---
 
# 🔒 Security
 
The application secures REST endpoints using Spring Security and JWT Authentication.
 
Authorization rules include:
 
| Role | Permissions |
|------|-------------|
| **ADMIN** | Create, update and delete events |
| **USER** | View events, view seats, create reservations, confirm reservations and cancel reservations |
 
All protected endpoints require a valid JWT token in the request header.
 
```http
Authorization: Bearer <jwt-token>
```
 
---
 
# ⚡ Concurrency Handling
 
To prevent multiple users from reserving the same seat simultaneously, the application uses **JPA Optimistic Locking**.
 
Each seat entity contains a version field:
 
```java
@Version
private Long version;
```
 
If two users attempt to reserve the same seat at the same time:
 
- the first transaction succeeds,
- the second transaction fails,
- data consistency is preserved,
- duplicate reservations are prevented.
---
 
# ⏰ Scheduled Reservation Expiration
 
Reservations remain in **HELD** status for a limited time.
 
A scheduled task periodically checks expired reservations and automatically:
 
- changes reservation status to **EXPIRED**
- updates the seat status to **AVAILABLE**
This ensures that seats are automatically released when users do not confirm their reservations.
 
---
 
# 🧪 Testing
 
The project includes both unit and integration tests to ensure application reliability and security.
 
## Unit Testing
 
Service layer components are tested using:
 
- JUnit 5
- Mockito
The unit tests cover:
 
- User registration
- User authentication
- Event management
- Reservation creation
- Reservation confirmation
- Reservation cancellation
- Business validation rules
- Exception scenarios
---
 
## Integration Testing
 
Security integration tests are implemented using:
 
- Spring Boot Test
- MockMvc
- Spring Security Test
Integration tests verify:
 
- Protected endpoint access
- JWT authentication flow
- Role-based authorization
- ADMIN and USER permissions
---
 
# ⚙️ Installation
 
## Prerequisites
 
- Java 23
- Maven
- PostgreSQL
- pgAdmin (Optional)
- IntelliJ IDEA or Eclipse
---
 
## Clone Repository
 
```bash
git clone https://github.com/OykuEyuboglu/event-reservation-api.git
```
 
---
 
## Navigate to Project
 
```bash
cd event-reservation-api
```
 
---
 
## Configure Database
 
Update the database configuration inside:
 
```
src/main/resources/application.properties
```
 
Example:
 
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/event_reservation_db
spring.datasource.username=postgres
spring.datasource.password=your_password
 
spring.jpa.hibernate.ddl-auto=update
```
 
---
 
## Install Dependencies
 
```bash
mvn clean install
```
 
---
 
## Run Application
 
```bash
mvn spring-boot:run
```
 
The application starts at:
 
```
http://localhost:8080
```
 
---
 
## Open Swagger UI
 
```
http://localhost:8080/swagger-ui/index.html
```
 
---
 
# 📚 Key Concepts Covered
 
This project demonstrates practical experience with:
 
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- PostgreSQL
- Spring Security
- JWT Authentication
- Role-Based Authorization
- REST API Design
- Layered Architecture
- DTO Pattern
- MapStruct
- Global Exception Handling
- Bean Validation
- Scheduled Tasks
- Optimistic Locking
- Concurrency Control
- Unit Testing with Mockito
- Integration Testing with MockMvc
- Swagger / OpenAPI
- SLF4J Logging
- Clean Code Principles
---

# 👩‍💻 Author
 
**Dila Öykü Eyüboğlu**
