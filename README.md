# 📓 JournalApp

A secure RESTful Journal Application built with **Spring Boot**, **Spring Security**, **JWT Authentication**, and **MongoDB**. The application allows users to securely manage their personal journal entries while enforcing authentication, authorization, input validation, and proper error handling.

The project follows a clean layered architecture consisting of **Controllers**, **Services**, **Repositories**, and **Models**, making the codebase modular, maintainable, and scalable.

---

# 🚀 Features

## 🔐 Authentication & Authorization

- User Registration
- User Login
- JWT-based Authentication
- Stateless Authentication
- Password Encryption using BCrypt
- Role-Based Authorization (USER, ADMIN)
- Spring Security Integration

---

## 👤 User Management

- Register new users
- Update user profile
- Delete account
- Retrieve authenticated user details
- Admin-specific user management

---

## 📝 Journal Entry Management

- Create journal entries
- Retrieve all personal journal entries
- Retrieve a specific journal entry
- Update journal entries
- Delete journal entries
- Ownership validation (users can only access their own entries)

---

## ✅ Validation & Error Handling

- DTO-based request validation
- Bean Validation (`@Valid`)
- Global Exception Handling
- Custom Exception classes
- Standardized API error responses
- Proper HTTP status codes using `ResponseEntity`

---

# 🛠 Tech Stack

- Java 21
- Spring Boot
- Spring Security
- Spring Data MongoDB
- MongoDB
- JWT (JJWT)
- Bean Validation
- Maven
- Lombok

---

# 📂 Project Structure

```text
src
└── main
    ├── java
    │   └── dev.anamika.journalApp
    │       ├── config
    │       ├── controllers
    │       ├── dto
    │       ├── exception
    │       ├── filter
    │       ├── models
    │       ├── repositories
    │       ├── services
    │       ├── utils
    │       └── JournalApplication.java
    │
    └── resources
        ├── static
        ├── templates
        └── application.properties
```

---

# 📦 Package Responsibilities

| Package | Responsibility |
|----------|----------------|
| **config** | Spring Security configuration |
| **controllers** | REST API endpoints |
| **dto** | Request and response DTOs |
| **exception** | Global exception handler and custom exceptions |
| **filter** | JWT Authentication Filter |
| **models** | MongoDB document models |
| **repositories** | Database access layer |
| **services** | Business logic |
| **utils** | JWT utilities and helper classes |

---

# 🏗 Architecture

```text
                Client
                   │
                   ▼
          JWT Authentication
                   │
                   ▼
           Security Filter
                   │
                   ▼
             Controllers
                   │
                   ▼
              Services
                   │
                   ▼
           Repositories
                   │
                   ▼
               MongoDB
```

---

# 🔐 Authentication Flow

```text
User Login
     │
     ▼
Authenticate Credentials
     │
     ▼
Generate JWT Token
     │
     ▼
Client Stores JWT
     │
     ▼
JWT Sent in Authorization Header
     │
     ▼
JWT Filter Validates Token
     │
     ▼
Protected API Access
```

---

# 📌 API Overview

## Public Endpoints

```http
POST /public/signup
POST /public/login
```

---

## User Endpoints

```http
GET    /journal
GET    /journal/{id}
POST   /journal
PUT    /journal/{id}
DELETE /journal/{id}

GET    /user
PUT    /user
DELETE /user
```

---

## Admin Endpoints

```http
GET    /admin/users
POST   /admin/create-admin
PUT    /admin/user-role
DELETE /admin/user/{username}
```

---

# 🗄 Database Models

## User

```json
{
  "id": "...",
  "userName": "john",
  "password": "encrypted-password",
  "roles": [
    "USER"
  ],
  "journalEntries": []
}
```

---

## Journal Entry

```json
{
  "id": "...",
  "title": "My Journal",
  "content": "Today was productive...",
  "date": "2026-06-26"
}
```

---

# ⚙️ Setup

## Clone Repository

```bash
git clone https://github.com/<your-username>/JournalApp.git
```

Move into the project directory:

```bash
cd JournalApp
```

---

## Configure MongoDB

Update your `application.properties` file:

```properties
spring.data.mongodb.uri=YOUR_MONGODB_URI
```

---

## Run the Application

```bash
mvn spring-boot:run
```

The application will start at:

```text
http://localhost:8080
```

---

# 📈 Current Status

## ✅ Completed

- JWT Authentication
- Spring Security
- CRUD APIs
- MongoDB Integration
- DTO Layer
- Bean Validation
- Global Exception Handling
- Custom Exceptions
- Role-Based Authorization
- Ownership Validation
- Layered Architecture

---

## 🚀 Future Improvements

- Logging using SLF4J & Logback
- Swagger / OpenAPI Documentation
- Unit Testing (JUnit & Mockito)
- Integration Testing
- Docker Support
- Refresh Tokens
- Email Verification
- Password Reset

---

# 👩‍💻 Author

**Anamika Rai**

Third-year B.Tech Computer Science student passionate about Backend Development, Java, Spring Boot, REST APIs, and scalable application development.

---

## ⭐ If you found this project useful, consider giving it a star!