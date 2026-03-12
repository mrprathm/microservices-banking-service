# 🏦 Microservices Banking Service

> **Enterprise Banking System built with Spring Boot Microservices Architecture**  
> **Author:** Pratham Rathod  
> **Email:** prathamrathod200@gmail.com | **Phone:** +91-9890394356  
> **Location:** Pune, Maharashtra  
> **LinkedIn:** [Pratham Rathod](https://linkedin.com/in/pratham-rathod) | **GitHub:** [prathamrathod](https://github.com/prathamrathod)

---

## 📋 Project Overview

A production-grade **Microservices Banking System** demonstrating modern backend architecture with:
- **5 independent microservices** communicating via REST APIs
- **Service Discovery** using Netflix Eureka
- **API Gateway** for unified entry point
- **MySQL** databases (separate DB per service)
- **Swagger/OpenAPI** documentation for every service
- **Spring Boot 3.2** with Java 17

---

## 🏗️ Architecture

```
                    ┌─────────────────────┐
                    │    API Gateway       │
                    │    (Port: 8080)      │
                    └──────────┬──────────┘
                               │
            ┌──────────────────┼──────────────────┐
            │                  │                  │
    ┌───────▼──────┐  ┌───────▼──────┐  ┌───────▼──────┐
    │  Customer    │  │  Account     │  │ Transaction  │
    │  Service     │  │  Service     │  │  Service     │
    │  Port: 8081  │  │  Port: 8082  │  │  Port: 8083  │
    └──────────────┘  └──────────────┘  └──────────────┘
            │                                     │
    ┌───────▼──────┐                   ┌──────────▼───┐
    │   Loan       │                   │ Notification │
    │   Service    │                   │   Service    │
    │  Port: 8084  │                   │  Port: 8085  │
    └──────────────┘                   └──────────────┘
                               │
                    ┌──────────▼──────────┐
                    │   Eureka Server     │
                    │   (Port: 8761)      │
                    └─────────────────────┘
```

---

## 🚀 Microservices

| Service              | Port | Database                   | Description                        |
|----------------------|------|----------------------------|------------------------------------|
| Eureka Server        | 8761 | -                          | Service Discovery & Registration   |
| Config Server        | 8888 | -                          | Centralized Configuration          |
| API Gateway          | 8080 | -                          | Single entry point, routing        |
| Customer Service     | 8081 | banking_customer_db        | KYC, Customer CRUD                 |
| Account Service      | 8082 | banking_account_db         | Account management, balance        |
| Transaction Service  | 8083 | banking_transaction_db     | Fund transfers, statements         |
| Loan Service         | 8084 | banking_loan_db            | Loan lifecycle management          |
| Notification Service | 8085 | banking_notification_db    | Email/SMS alerts                   |

---

## 💡 Key Features

### Customer Service
- ✅ Customer Registration with KYC (PAN, Aadhaar validation)
- ✅ Customer profile management
- ✅ KYC status updates
- ✅ Search customers by name/email/phone
- ✅ Pagination & sorting

### Account Service
- ✅ Open savings/current/FD/salary accounts
- ✅ Deposit & Withdrawal operations
- ✅ Balance enquiry
- ✅ Account status management (freeze/unfreeze)
- ✅ Minimum balance enforcement
- ✅ Interest rate configuration

### Transaction Service
- ✅ Fund transfers (NEFT, RTGS, IMPS, UPI)
- ✅ Transaction history with date range filters
- ✅ Mini statement generation
- ✅ Transaction reversal
- ✅ Unique UTR/reference number generation
- ✅ Credit/Debit summaries

### Loan Service
- ✅ Apply for loans (Home, Personal, Car, Education, Business, Gold)
- ✅ Loan approval/rejection workflow
- ✅ Loan disbursement
- ✅ EMI calculation using standard formula
- ✅ EMI payment tracking
- ✅ Loan closure on full repayment
- ✅ Outstanding amount tracking

### Notification Service
- ✅ Email & SMS notifications
- ✅ Transaction alerts
- ✅ Loan status updates
- ✅ Notification history per customer

---

## 🛠️ Tech Stack

| Category    | Technology                                     |
|-------------|------------------------------------------------|
| Language    | Java 17                                        |
| Framework   | Spring Boot 3.2, Spring Cloud 2023.0.0         |
| Database    | MySQL 8.x (separate DB per service)            |
| ORM         | Spring Data JPA + Hibernate                    |
| Discovery   | Netflix Eureka                                 |
| Gateway     | Spring Cloud Gateway                           |
| API Docs    | SpringDoc OpenAPI 3 (Swagger UI)               |
| Build Tool  | Maven (Multi-module)                           |
| Logging     | SLF4J + Logback                                |
| Validation  | Jakarta Bean Validation                        |

---

## ⚙️ Prerequisites

- **Java 17** or higher
- **Maven 3.8+**
- **MySQL 8.x** running on localhost:3306
- **IntelliJ IDEA** (recommended)

---

## 🗄️ Database Setup

```sql
-- Run this in MySQL Workbench or MySQL CLI
CREATE DATABASE IF NOT EXISTS banking_customer_db;
CREATE DATABASE IF NOT EXISTS banking_account_db;
CREATE DATABASE IF NOT EXISTS banking_transaction_db;
CREATE DATABASE IF NOT EXISTS banking_loan_db;
CREATE DATABASE IF NOT EXISTS banking_notification_db;

-- Grant permissions (if using non-root user)
GRANT ALL PRIVILEGES ON banking_customer_db.* TO 'root'@'localhost';
GRANT ALL PRIVILEGES ON banking_account_db.* TO 'root'@'localhost';
GRANT ALL PRIVILEGES ON banking_transaction_db.* TO 'root'@'localhost';
GRANT ALL PRIVILEGES ON banking_loan_db.* TO 'root'@'localhost';
GRANT ALL PRIVILEGES ON banking_notification_db.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

> **Note:** Tables are created automatically via Hibernate (`ddl-auto: update`)

---

## 🚦 How to Run

### Step 1: Start services in this ORDER:

```bash
# 1. Eureka Server (Service Discovery) - Start FIRST
cd eureka-server
mvn spring-boot:run

# 2. Config Server
cd config-server
mvn spring-boot:run

# 3. API Gateway
cd api-gateway
mvn spring-boot:run

# 4. Microservices (can run in any order after above)
cd customer-service && mvn spring-boot:run
cd account-service && mvn spring-boot:run
cd transaction-service && mvn spring-boot:run
cd loan-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run
```

### Or run entire project from root:
```bash
mvn clean install -DskipTests
```

---

## 🔑 Credentials

| Service         | Username | Password |
|-----------------|----------|----------|
| Eureka Server   | admin    | Root     |
| MySQL           | root     | Root     |

---

## 🌐 API Endpoints

### Swagger UI (per service):
- Customer: http://localhost:8081/swagger-ui.html
- Account: http://localhost:8082/swagger-ui.html
- Transaction: http://localhost:8083/swagger-ui.html
- Loan: http://localhost:8084/swagger-ui.html
- Notification: http://localhost:8085/swagger-ui.html
- Eureka Dashboard: http://localhost:8761

### Key API Examples:

#### Customer Service
```
POST   /api/v1/customers          - Register new customer
GET    /api/v1/customers/{id}     - Get customer by ID
GET    /api/v1/customers/search?query=pratham  - Search customers
PATCH  /api/v1/customers/{id}/kyc-status?kycStatus=VERIFIED
```

#### Account Service
```
POST   /api/v1/accounts                          - Open account
POST   /api/v1/accounts/{accountNumber}/deposit?amount=5000
POST   /api/v1/accounts/{accountNumber}/withdraw?amount=2000
GET    /api/v1/accounts/{accountNumber}/balance
GET    /api/v1/accounts/customer/{customerId}
```

#### Transaction Service
```
POST   /api/v1/transactions                                - Create transaction
GET    /api/v1/transactions/account/{accountNumber}        - Account statement
GET    /api/v1/transactions/account/{accountNumber}/type/CREDIT
POST   /api/v1/transactions/{reference}/reverse
```

#### Loan Service
```
POST   /api/v1/loans/apply             - Apply for loan
PUT    /api/v1/loans/{loanNumber}/approve
PUT    /api/v1/loans/{loanNumber}/disburse
POST   /api/v1/loans/{loanNumber}/pay-emi?amount=15000
GET    /api/v1/loans/calculate-emi?principal=500000&annualRate=8.5&tenureMonths=60
```

---

## 📁 Project Structure

```
microservices-banking-service/
├── pom.xml                          # Parent POM
├── README.md
├── eureka-server/                   # Service Discovery
├── config-server/                   # Centralized Config
├── api-gateway/                     # API Gateway (Port 8080)
├── customer-service/                # Customer Management
│   └── src/main/java/com/banking/customerservice/
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── entity/
│       ├── dto/
│       └── exception/
├── account-service/                 # Account Management
├── transaction-service/             # Transaction Management
├── loan-service/                    # Loan Management
└── notification-service/            # Notifications
```

---

## 👨‍💻 Developer

**Pratham Rathod**  
Full Stack Java Developer | Pune, Maharashtra  
📧 prathamrathod200@gmail.com  
📱 +91-9890394356  

**Skills:** Java | Spring Boot | Hibernate | REST APIs | ReactJS | MySQL | Microservices

---

## 📝 License

This project is developed for educational and portfolio purposes.
