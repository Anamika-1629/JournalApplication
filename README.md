# JournalApp

A backend REST API built with **Spring Boot** and **MongoDB** for managing personal journal entries.

The project follows a layered architecture using Controllers, Services, Repositories, and Models to provide a clean and scalable codebase.

---

## 🚀 Features

### User Management

* Create users
* Retrieve user details
* Update user information
* Delete users

### Journal Entry Management

* Create journal entries
* Retrieve journal entries
* Update journal entries
* Delete journal entries

### Relationship Mapping

* Each user can own multiple journal entries.
* Journal entries are linked with users using MongoDB references.

### API Responses

* Uses `ResponseEntity` for proper HTTP status handling.
* Supports success and error responses.

---

## 🛠 Tech Stack

* Java 21
* Spring Boot
* Spring Web
* Spring Data MongoDB
* MongoDB
* Maven
* Lombok

---

## 📂 Project Structure

```text
src
└── main
    ├── java
    │   └── dev.anamika.journalApp
    │       ├── controllers
    │       │   ├── HealthCheck
    │       │   ├── JournalEntryController
    │       │   └── UserController
    │       │
    │       ├── models
    │       │   ├── JournalEntry
    │       │   └── Users
    │       │
    │       ├── repositories
    │       │   ├── JournalEntryRepository
    │       │   └── UserRepository
    │       │
    │       ├── services
    │       │   ├── JournalEntryService
    │       │   └── UserService
    │       │
    │       └── JournalApplication
    │
    └── resources
        ├── static
        ├── templates
        └── application.properties
```

---

## 🏗 Architecture

```text
Client
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

### Responsibilities

#### Controllers

Handle incoming HTTP requests and return responses.

#### Services

Contain business logic and coordinate operations between controllers and repositories.

#### Repositories

Interact with MongoDB using Spring Data MongoDB.

#### Models

Represent database documents and relationships.

---

## ⚙️ Setup

### Configure MongoDB

Update your `application.properties`:

```properties
spring.data.mongodb.uri=YOUR_MONGODB_URI
```

### Run the Application

```bash
mvn spring-boot:run
```

Application starts on:

```text
http://localhost:8080
```

---

## 📌 Current Status

### Implemented

* User CRUD operations
* Journal Entry CRUD operations
* Service layer abstraction
* MongoDB integration
* User ↔ Journal Entry relationship mapping
* Basic health check endpoint

### In Progress

* Ownership validation for journal entries
* Better exception handling
* DTO implementation
* Authentication & Authorization
* Spring Security integration
* JWT-based authentication

---

🚧 Under Development