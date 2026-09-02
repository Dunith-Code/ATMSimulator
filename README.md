# 🏦 ATM Simulator – Full Stack Banking System

[![Java](https://img.shields.io/badge/Java-17%2B-blue)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-green)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue)](https://www.docker.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A production-grade, full-stack ATM simulation built to demonstrate a complete understanding of the Software Development Life Cycle (SDLC). This project covers everything from architectural design (using 5 UML diagrams) to implementation, security, testing, containerization, and microservices integration.

---

## 📐 1. System Architecture & Design (The 5 Diagrams)

Before writing a single line of code, I adopted a **"Diagram-First" approach** to ensure the system was scalable, robust, and secure. The architecture is divided into four distinct layers: **Client (UI)**, **Controller (API)**, **Service (Business Logic)**, and **Repository (Data Access)**.

- **[Use Case Diagram](/docs/diagrams/1_UseCase.png)** – Defines the actors (Customer, Bank Admin) and their interactions with the system, such as Withdraw, Deposit, and Balance Inquiry.
- **[Sequence Diagram](/docs/diagrams/2_Sequence.png)** – Maps the chronological flow of a financial transaction. It demonstrates the handshake between the ATM UI, the Proxy Server, and the Database, ensuring the order of operations is correct.
- **[Class Diagram](/docs/diagrams/3_Class.png)** – Outlines the static structure of the backend. It shows the OOP relationships, including the `Account` Entity, the `ATMService` (Business Logic), and the `ATMController` (REST endpoints).
- **[ER Diagram](/docs/diagrams/4_ER.png)** – The logical blueprint of the PostgreSQL database, showing the `accounts` table with fields like `cardNumber`, `hashedPin`, `balance`, and `nic` (for audit trails).
- **[Deployment Diagram](/docs/diagrams/5_Deployment.png)** – Details the DevOps architecture, showing how the Spring Boot backend, Node.js UI server, PostgreSQL database, and Python ML microservice are containerized using Docker.

---

## 🧠 2. Object-Oriented Programming (OOP) Principles

The project strictly adheres to **SOLID** principles to ensure maintainability and scalability.

- **Encapsulation**: The `Account` entity protects sensitive data by marking fields as `private` and exposing them only through controlled getters and setters. The BCrypt hashed PIN is never exposed as plain text.
- **Abstraction**: The system is divided into clean layers (Controller ↔ Service ↔ Repository). The `ATMController` (the "Waiter") doesn't care about the database logic, and the `ATMService` (the "Chef") doesn't care about how HTTP requests are parsed. This isolation makes the code easier to test and modify.
- **Inheritance & Polymorphism**: The `AccountRepository` extends Spring Data JPA’s `JpaRepository`, inheriting robust CRUD methods. Furthermore, the **Strategy Pattern** is implemented via the `FraudDetector` interface. The system can dynamically swap between a `RuleBasedFraudDetector` and a `MLFraudDetector` without changing the core withdrawal logic, showcasing runtime polymorphism and the Open/Closed Principle.

---

## ⚙️ 3. Data Structures & Algorithms (DSA) Integration

While banking logic relies on complex algorithms, the application leverages key data structures to ensure performance, data integrity, and precision.

- **HashMap (DSA)**: The REST API utilizes `Map<String, String>` to handle flexible JSON request bodies. This allows the system to parse dynamic attributes (like `cardNumber` and `amount`) with O(1) average time complexity, ensuring high throughput under load.
- **Precision with BigDecimal**: To avoid floating-point rounding errors (which are catastrophic in finance), all monetary calculations (balance addition, subtraction, and comparisons) strictly use `BigDecimal`. This ensures mathematical accuracy down to the cent.
- **Optional Handling**: The Repository returns `Optional<Account>` when querying the database. This functional programming pattern prevents `NullPointerException` crashes and forces explicit handling of "Account Not Found" scenarios.
- **ACID Transactions**: The `@Transactional` annotation ensures atomicity. When processing a withdrawal, the sequence of "Check Balance → Update Balance → Save" is treated as a single unit of work. If any step fails, the entire transaction is automatically rolled back, preserving data consistency.

---

## 🗄️ 4. Database Management System (DBMS) & ACID Compliance

The application uses **PostgreSQL** as the relational database, showcasing a deep understanding of RDBMS principles.

- **Schema Design**: The database follows the **3rd Normal Form (3NF)** as seen in the ER Diagram. The `accounts` table uses `cardNumber` as a unique key (`UNIQUE` constraint) to prevent duplicate accounts and ensure referential integrity.
- **Concurrency Control**: To handle simultaneous withdrawal requests from different devices, the system implements **Pessimistic Locking**. The `@Lock(LockModeType.PESSIMISTIC_WRITE)` annotation transforms the SELECT query into `SELECT ... FOR UPDATE`. This locks the specific account row during the transaction, preventing race conditions (e.g., two users withdrawing the same $100 from a $100 balance, which would cause an overdraft).
- **Data Integrity**: The system enforces `NOT NULL` constraints on critical fields (PIN, NIC, Card Number), ensuring that no incomplete records exist in the database. The `TRUNCATE` and `RESTART IDENTITY` commands are used in the testing lifecycle to reset the state cleanly.

---

## 🔐 5. Security & Auditability

Security was a primary concern to mimic real-world banking standards.

- **PIN Hashing**: User PINs are never stored in plain text. The `BCryptPasswordEncoder` hashes the PIN with a salt, making it computationally infeasible for attackers to reverse-engineer credentials, even if the database is compromised.
- **CORS Configuration**: The Spring Boot backend is configured to accept requests only from the trusted Node.js UI origin (`@CrossOrigin(origins = "http://localhost:3000")`), preventing Cross-Site Request Forgery (CSRF) attacks.
- **Audit Trails**: For deposit transactions, the system mandates a **NIC (National Identity Card)** instead of a PIN. This separates the concept of *Authorization* (PIN for owner) from *Traceability* (NIC for depositor), providing a clear audit trail for regulatory compliance.

---

## 🚀 6. How to Run the Project

### Option A: Run with Docker (Recommended - One Command)
The application is fully containerized for zero-friction execution.

1.  Ensure you have **Docker Desktop** installed and running.
2.  Build the Java JAR:
    ```bash
    ./mvnw.cmd clean package -DskipTests

---
**Dunith Desitha Athukorala**