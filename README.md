# 🏦 Banking System Backend (Spring Boot)

A secure and scalable banking backend application built using Spring Boot. This system allows users to register, login, create accounts, transfer money, and view transaction history with proper security, validation, and audit tracking.

---

## 🚀 Features

### 🔐 Authentication & Security

* User Registration & Login
* JWT-based Authentication
* Role-based Authorization (USER / ADMIN ready)
* Password encryption using BCrypt

---

### 🏦 Account Management

* Create Bank Account (SAVINGS / CURRENT)
* View User Accounts
* Secure account ownership validation

---

### 💸 Transactions

* Transfer money between accounts
* Balance validation (no overdraft)
* Atomic transactions using `@Transactional`
* Concurrency handling using Pessimistic Locking
* Debit & Credit transaction tracking

---

### 📜 Transaction History

* View all transactions (sent & received)
* Clean API response using DTO

---

### 🧾 Audit Logging

Tracks:

* User registration
* Account creation
* Money transfers

Stores action, status, and user details

---

### ✅ Validation

* Input validation using Jakarta Validation
* Prevents invalid or empty requests

---

### ⚠️ Exception Handling

* Custom exceptions:

    * AccountNotFoundException
    * InsufficientBalanceException
    * UnauthorizedAccessException
* Global exception handler

---

### 📘 API Documentation

* Swagger UI integrated
* Test APIs directly from browser

---

### 🧪 Unit Testing

* Mockito-based unit tests
* Covers:

    * Successful transfer
    * Insufficient balance
    * Account not found

---

## 🏗️ Tech Stack

* Java 17
* Spring Boot
* Spring Security
* JWT (Authentication)
* Spring Data JPA
* MySQL
* Lombok
* Swagger (OpenAPI)
* JUnit & Mockito

---

## 📂 Project Structure

com.bank
├── controller
├── service
├── service.impl
├── repository
├── entity
├── dto
├── exception
├── security
└── config

---

## 🔄 Request Flow (Money Transfer)

1. User sends request with JWT token
2. JWT Filter validates token
3. Controller receives request
4. Service handles business logic
5. Repository interacts with DB
6. Transaction + Audit logs saved
7. Response returned

---

## 🔐 JWT Flow

1. User logs in with credentials
2. Server generates JWT token
3. Token sent to user
4. User sends token in every request
5. JWT Filter validates token
6. Access granted or denied

---

## ▶️ How to Run

1. Clone the repository:
   git clone https://github.com/tarunyendu-developer/banking-system.git

2. Configure MySQL in application.properties

3. Run the project:
   mvn spring-boot:run

4. Access Swagger UI:
   http://localhost:8080/swagger-ui/index.html

---

## 🧪 Sample APIs

Register
POST /api/users/register

Login
POST /api/users/login

Create Account
POST /api/accounts

Transfer Money
POST /api/transactions/transfer

Transaction History
GET /api/transactions/history/{accountNumber}

---

## 🎯 Future Enhancements

* Admin dashboard
* Pagination for transactions
* Email/SMS notifications
* Refresh token mechanism

---

## 👨‍💻 Author

Tarun Yendu
Full Stack Developer

---

## ⭐ Conclusion

This project demonstrates:

* Secure backend development
* Clean architecture
* Real-world banking logic
* Production-level practices
