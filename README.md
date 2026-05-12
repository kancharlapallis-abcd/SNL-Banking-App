# SNL Banking Application - Complete Setup Guide

A comprehensive banking application built with modern technologies following HDFC banking application patterns.

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [Technology Stack](#technology-stack)
3. [Folder Structure](#folder-structure)
4. [Installation & Setup](#installation--setup)
5. [Running the Application](#running-the-application)
6. [API Documentation](#api-documentation)
7. [Database Schema](#database-schema)
8. [Features](#features)
9. [Testing](#testing)

## 🎯 Project Overview

SNL Banking Application is a full-stack banking system designed to provide:
- **Core Banking Services**: Accounts, Transfers, Balance Inquiry
- **Enhanced Services**: Bill Payments, Loan Management
- **Advanced Features**: Investment Services, Financial Management Tools
- **Security**: Role-based access control, audit logging, encryption

## 🛠️ Technology Stack

### Backend
| Component | Technology | Version |
|-----------|-----------|---------|
| Framework | Spring Boot | 3.1.5 |
| Language | Java | 17 |
| Database | PostgreSQL | 12+ |
| Build Tool | Maven | 3.6+ |
| API Documentation | Swagger/OpenAPI | 2.1.0 |
| Testing | JUnit 5, Mockito | Latest |

### Frontend (Planned)
- React.js
- Redux/Context API
- Material-UI / Ant Design
- Axios

## 📁 Folder Structure

```
SNLBanking/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/snlbanking/
│   │   │   │   ├── SNLBankingApplication.java    # Main Spring Boot App
│   │   │   │   ├── controller/                   # REST Controllers (UserController, AccountController, etc.)
│   │   │   │   ├── service/                      # Service Interfaces & Implementations
│   │   │   │   │   ├── UserService.java
│   │   │   │   │   ├── AccountService.java
│   │   │   │   │   ├── TransactionService.java
│   │   │   │   │   └── impl/                     # Service Implementations
│   │   │   │   ├── repository/                   # JPA Repositories
│   │   │   │   │   ├── UserRepository.java
│   │   │   │   │   ├── AccountRepository.java
│   │   │   │   │   ├── TransactionRepository.java
│   │   │   │   │   └── ...
│   │   │   │   ├── entity/                       # JPA Entities
│   │   │   │   │   ├── User.java
│   │   │   │   │   ├── Account.java
│   │   │   │   │   ├── Transaction.java
│   │   │   │   │   ├── Loan.java
│   │   │   │   │   ├── BillPayment.java
│   │   │   │   │   └── AuditLog.java
│   │   │   │   ├── dto/                         # Data Transfer Objects
│   │   │   │   │   ├── UserRegistrationDTO.java
│   │   │   │   │   ├── AccountResponseDTO.java
│   │   │   │   │   ├── FundTransferDTO.java
│   │   │   │   │   └── APIResponseDTO.java
│   │   │   │   ├── exception/                   # Custom Exceptions
│   │   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   │   ├── DuplicateResourceException.java
│   │   │   │   │   ├── InsufficientBalanceException.java
│   │   │   │   │   ├── InvalidOperationException.java
│   │   │   │   │   ├── ErrorResponseDTO.java
│   │   │   │   │   └── GlobalExceptionHandler.java
│   │   │   │   ├── config/                      # Configuration Classes
│   │   │   │   └── util/                        # Utility Classes
│   │   │   └── resources/
│   │   │       ├── application.yml              # Application Configuration
│   │   │       └── db/migration/                # Database Migrations
│   │   │           ├── V1__Initial_Schema.sql
│   │   │           └── V2__Insert_Test_Data.sql
│   │   └── test/
│   │       └── java/com/snlbanking/
│   │           ├── service/                     # Service Unit Tests
│   │           │   ├── UserServiceTest.java
│   │           │   └── AccountServiceTest.java
│   │           └── controller/                  # Controller Tests
│   │               └── UserControllerTest.java
│   ├── pom.xml                                  # Maven Dependencies
│   ├── .gitignore                               # Git Ignore
│   └── README.md                                # Backend Documentation
│
├── frontend/                                    # React Frontend (To be set up)
│   ├── src/
│   │   ├── components/                          # Reusable Components
│   │   ├── pages/                               # Page Components
│   │   ├── services/                            # API Services
│   │   ├── redux/                               # Redux State Management
│   │   ├── styles/                              # CSS/SCSS
│   │   └── App.js
│   ├── package.json
│   └── README.md
│
└── README.md                                    # Project Root Documentation
```

## 🚀 Installation & Setup

### Prerequisites
```bash
# Check Java Version
java -version
# Output: openjdk version "17" or higher

# Check Maven Version
mvn -version
# Output: Apache Maven 3.6 or higher

# Check PostgreSQL
psql --version
# Output: psql (PostgreSQL) 12 or higher
```

### Step 1: Database Setup

#### Create Database
```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE snl_banking;
CREATE USER snl_user WITH PASSWORD 'snl_password';
GRANT ALL PRIVILEGES ON DATABASE snl_banking TO snl_user;
ALTER DATABASE snl_banking OWNER TO snl_user;

# Exit
\q
```

#### Verify Database
```bash
psql -U snl_user -d snl_banking
# Should connect successfully
```

### Step 2: Backend Setup

```bash
# Navigate to backend directory
cd SNLBanking/backend

# Update application.yml with your database credentials
# File: src/main/resources/application.yml
```

### Step 3: Build & Run

```bash
# Clean build
mvn clean install

# Run application
mvn spring-boot:run

# Or using JAR
java -jar target/snl-banking-app-1.0.0-SNAPSHOT.jar
```

### Step 4: Verify Setup

- **API Documentation**: http://localhost:8080/api/swagger-ui.html
- **API Docs JSON**: http://localhost:8080/api/v3/api-docs
- **Health Check**: http://localhost:8080/api/actuator/health

## 📚 Running the Application

### Development Mode
```bash
mvn spring-boot:run
```

### Production Mode
```bash
# Build JAR
mvn clean package -DskipTests

# Run JAR
java -jar target/snl-banking-app-1.0.0-SNAPSHOT.jar --spring.profiles.active=prod
```

### Docker (Optional)
```bash
# Build Docker image
docker build -t snl-banking-app .

# Run container
docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/snl_banking snl-banking-app
```

## 📖 API Documentation

### User Management APIs
```bash
# Register User
POST /api/v1/users/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "Password@123",
  "mobileNumber": "9876543210",
  "panNumber": "AAAAA0001A",
  "aadharNumber": "123456789012"
}

# Get User
GET /api/v1/users/1

# Update User
PUT /api/v1/users/1

# Get All Users (Paginated)
GET /api/v1/users?page=0&size=10

# Activate User
POST /api/v1/users/1/activate

# Change Password
POST /api/v1/users/1/change-password?oldPassword=Old@123&newPassword=New@123
```

### Account Management APIs
```bash
# Create Account
POST /api/v1/accounts
{
  "userId": 1,
  "accountType": "SAVINGS",
  "ifscCode": "HDFC0001234",
  "micRCode": "HDFC000123"
}

# Get Account
GET /api/v1/accounts/1

# Get Account Balance
GET /api/v1/accounts/1/balance

# Get User Accounts
GET /api/v1/accounts/user/1

# Close Account
POST /api/v1/accounts/1/close

# Freeze Account
POST /api/v1/accounts/1/freeze
```

### Transaction APIs
```bash
# Transfer Funds
POST /api/v1/transactions/transfer
{
  "fromAccountId": 1,
  "toAccountNumber": "SNL1003000003",
  "amount": 5000.00,
  "description": "Payment to friend"
}

# Get Transactions
GET /api/v1/transactions/account/1

# Get Outgoing Transactions
GET /api/v1/transactions/account/1/outgoing

# Get Transaction History
GET /api/v1/transactions/account/1/history?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59

# Reverse Transaction
POST /api/v1/transactions/1/reverse
```

## 🗄️ Database Schema

### Core Tables

#### Users Table
```sql
- user_id (PRIMARY KEY)
- firstName, lastName
- email (UNIQUE), mobileNumber (UNIQUE)
- panNumber, aadharNumber
- address, city, state, pincode
- role (CUSTOMER, ADMIN, SUPPORT_STAFF)
- status (ACTIVE, INACTIVE, SUSPENDED, VERIFIED_PENDING)
- createdAt, updatedAt
- Indexes: email, mobile_number, status
```

#### Accounts Table
```sql
- account_id (PRIMARY KEY)
- accountNumber (UNIQUE)
- user_id (FOREIGN KEY)
- accountType (SAVINGS, CURRENT, SALARY)
- balance, minimumBalance
- status (ACTIVE, INACTIVE, CLOSED, FROZEN)
- ifscCode, micRCode
- createdAt, updatedAt
- Indexes: account_number, user_id, status
```

#### Transactions Table
```sql
- transaction_id (PRIMARY KEY)
- transactionReference (UNIQUE)
- fromAccount_id, toAccount_id (FOREIGN KEYS)
- amount, charges
- transactionType (INTRA_BANK, INTER_BANK, DEPOSIT, etc.)
- status (PENDING, COMPLETED, FAILED, REVERSED)
- description, referenceNumber
- createdAt
- Indexes: transaction_reference, from_account, to_account, status
```

#### Loans Table
```sql
- loan_id (PRIMARY KEY)
- loanNumber (UNIQUE)
- user_id (FOREIGN KEY)
- loanType, principalAmount, interestRate
- tenureMonths, emiAmount, remainingAmount
- status (APPLIED, APPROVED, REJECTED, ACTIVE, CLOSED)
- createdAt, updatedAt
```

#### BillPayments Table
```sql
- bill_payment_id (PRIMARY KEY)
- billReference (UNIQUE)
- account_id (FOREIGN KEY)
- billType (ELECTRICITY, WATER, GAS, MOBILE, etc.)
- billerName, billAmount
- status (PENDING, COMPLETED, FAILED, CANCELLED)
- createdAt, updatedAt
```

#### AuditLogs Table
```sql
- audit_log_id (PRIMARY KEY)
- user_id (FOREIGN KEY)
- action, description
- ipAddress, userAgent
- status (SUCCESS, FAILURE, UNAUTHORIZED)
- createdAt
```

## ✨ Features

### Phase 1: Core Banking ✅
- [x] User Registration & Login
- [x] Account Creation
- [x] Balance Inquiry
- [x] Fund Transfers (Intra-bank)
- [x] Transaction History
- [x] Password Management
- [x] User Profile Management
- [x] Account Status Management

### Phase 2: Enhanced Services 🔄
- [ ] Bill Payments (Utilities, Mobile, Credit Card)
- [ ] Loan Applications & Tracking
- [ ] EMI Calculations
- [ ] Customer Support Chat
- [ ] Complaint Ticketing

### Phase 3: Advanced Features 📅
- [ ] Investment Services (Mutual Funds)
- [ ] Stock Trading Integration
- [ ] Budget Management Tools
- [ ] Spending Analytics
- [ ] Mobile Check Deposit

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Run with Coverage Report
```bash
mvn clean test jacoco:report
```

### View Coverage
```bash
# Open in browser
open target/site/jacoco/index.html
```

### Test Sample Data
- **Test User 1**: rajesh.kumar@gmail.com
- **Test User 2**: priya.sharma@gmail.com
- **Test Admin**: admin@snlbanking.com

## 🔐 Security Considerations

1. **Authentication**: JWT Token based (to be implemented)
2. **Encryption**: Password hashing with BCrypt
3. **Validation**: Input validation at DTO level
4. **Audit Logging**: All user actions logged
5. **Error Handling**: Comprehensive exception handling
6. **SQL Injection**: Prevention through parameterized queries

## 📊 Performance Tips

1. Use pagination for large datasets
2. Enable database query caching
3. Use appropriate database indexes
4. Connection pooling with HikariCP
5. Monitor query performance with slow query logs

## 🐛 Troubleshooting

### Issue: Database Connection Failed
```
Solution: Verify PostgreSQL is running and credentials are correct
$ psql -U postgres -h localhost
```

### Issue: Port 8080 Already in Use
```
Solution: Change port in application.yml
server:
  port: 8081
```

### Issue: Maven Build Failure
```
Solution: Update dependencies
mvn clean install -U
```

## 📝 Notes
- **Regulatory Compliance**: This is a technical implementation. Real banking applications require KYC, AML, GDPR compliance
- **Security**: Change default credentials and JWT secret in production
- **Scalability**: Consider microservices, caching, and load balancing for production

## 📞 Support
For issues or questions, please create an issue in the repository.

## 📄 License
MIT License - See LICENSE file for details

---

**Last Updated**: January 2024
**Version**: 1.0.0
**Status**: In Development
