# SNL Banking Application - Backend

## Project Overview
SNL Banking Application is a comprehensive banking system built with Spring Boot 3.1.5 and PostgreSQL. It provides core banking functionalities including user management, account management, fund transfers, bill payments, and loan management.

## Technology Stack
- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Database**: PostgreSQL
- **Build Tool**: Maven
- **Authentication**: JWT
- **Documentation**: Swagger/OpenAPI
- **Testing**: JUnit 5, Mockito

## Project Structure
```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/snlbanking/
│   │   │   ├── controller/          # REST Controllers
│   │   │   ├── service/             # Business Logic
│   │   │   ├── repository/          # Data Access
│   │   │   ├── entity/              # JPA Entities
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── exception/           # Custom Exceptions
│   │   │   ├── config/              # Configuration Classes
│   │   │   ├── util/                # Utility Classes
│   │   │   └── SNLBankingApplication.java  # Main Application
│   │   └── resources/
│   │       ├── application.yml      # Application Configuration
│   │       └── db/migration/        # Database Migrations
│   └── test/
│       └── java/com/snlbanking/     # Unit Tests
├── pom.xml                          # Maven Configuration
└── README.md                        # This file
```

## Features Implemented

### Phase 1: Core Banking Functionalities ✓
- [x] User Authentication & Authorization
- [x] Account Management
- [x] Fund Transfers (Intra-bank)
- [x] Balance Enquiry
- [x] Transaction History

### Phase 2: Enhanced Services (In Progress)
- [ ] Bill Payments
- [ ] Loan Applications
- [ ] Customer Support Integration

### Phase 3: Advanced Features (Planned)
- [ ] Investment Services
- [ ] Personalized Financial Management
- [ ] Mobile Check Deposit

## Prerequisites
- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher

## Installation & Setup

### 1. Database Setup
```bash
# Create database
createdb snl_banking -U postgres

# Create user (optional)
# psql -U postgres
# CREATE ROLE snl_user WITH LOGIN PASSWORD 'snl_password';
# GRANT ALL PRIVILEGES ON DATABASE snl_banking TO snl_user;
```

### 2. Application Configuration
Update `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/snl_banking
    username: postgres
    password: postgres
```

### 3. Build Application
```bash
# Clean build
mvn clean install

# Skip tests
mvn clean install -DskipTests
```

### 4. Run Application
```bash
# Using Maven
mvn spring-boot:run

# Using JAR
java -jar target/snl-banking-app-1.0.0-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## API Documentation
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **API Docs**: http://localhost:8080/api/v3/api-docs

## Core API Endpoints

### User Management
- `POST /api/v1/users/register` - Register new user
- `GET /api/v1/users/{id}` - Get user by ID
- `PUT /api/v1/users/{id}` - Update user profile
- `GET /api/v1/users` - Get all users (paginated)
- `POST /api/v1/users/{id}/change-password` - Change password
- `POST /api/v1/users/{id}/activate` - Activate user
- `POST /api/v1/users/{id}/deactivate` - Deactivate user

### Account Management
- `POST /api/v1/accounts` - Create account
- `GET /api/v1/accounts/{id}` - Get account details
- `GET /api/v1/accounts/user/{userId}` - Get user's accounts
- `GET /api/v1/accounts/{id}/balance` - Get balance
- `POST /api/v1/accounts/{id}/close` - Close account
- `POST /api/v1/accounts/{id}/freeze` - Freeze account
- `POST /api/v1/accounts/{id}/unfreeze` - Unfreeze account

### Fund Transfer
- `POST /api/v1/transactions/transfer` - Transfer funds
- `GET /api/v1/transactions/account/{accountId}` - Get account transactions
- `GET /api/v1/transactions/{id}` - Get transaction details
- `POST /api/v1/transactions/{id}/reverse` - Reverse transaction

## Testing

### Run Unit Tests
```bash
mvn test
```

### Run Tests with Coverage Report
```bash
mvn test jacoco:report
```

### View Coverage Report
```bash
# Report location: target/site/jacoco/index.html
```

## Database Schema

### Users Table
- Stores user/customer information
- Includes profile and KYC details
- Status tracking (ACTIVE, INACTIVE, SUSPENDED, VERIFIED_PENDING)

### Accounts Table
- Bank account information
- Balance tracking
- Multiple accounts per user (Savings, Current, Salary)

### Transactions Table
- Transaction records
- Status tracking (PENDING, COMPLETED, FAILED, REVERSED)
- Intra-bank and inter-bank transfers

### Loans Table
- Loan application tracking
- Status management
- EMI calculation

### Bill Payments Table
- Bill payment records
- Multiple bill types (Electricity, Water, Mobile, etc.)

### Audit Logs Table
- User action tracking
- Security auditing
- Compliance requirements

## Security Features
- Password Hashing (using PasswordEncoder)
- Input Validation (Jakarta Validation)
- SQL Injection Prevention (Parameterized Queries)
- CORS Configuration
- Rate Limiting (Implementation pending)
- Audit Logging

## Error Handling
Global exception handling with custom exceptions:
- `ResourceNotFoundException` - Resource not found
- `DuplicateResourceException` - Duplicate resource
- `InsufficientBalanceException` - Insufficient balance
- `InvalidOperationException` - Invalid operation

## Logging
- Configured with SLF4J
- Log levels: DEBUG for application, INFO for general
- Log files in `logs/` directory

## Configuration Properties
- JWT Secret: Configure in `application.yml`
- JWT Expiration: 24 hours (configurable)
- Transaction Charge: ₹10 per transaction
- Minimum Balance: Varies by account type

## Maven Dependencies
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Validation
- PostgreSQL Driver
- Lombok
- SpringDoc OpenAPI (Swagger)
- JWT Libraries
- JUnit 5
- Mockito

## Performance Considerations
- Database indexing on frequently queried columns
- Pagination support for large datasets
- Read-only transactions where applicable
- Connection pooling with HikariCP
- Lazy loading for associations

## Future Enhancements
1. **Authentication & Authorization**
   - OAuth 2.0 integration
   - Two-factor authentication
   - Role-based access control

2. **Advanced Features**
   - Investment portfolio management
   - Credit scoring
   - Fraud detection
   - Mobile app integration

3. **Infrastructure**
   - Kubernetes deployment
   - Microservices architecture
   - Event streaming (Kafka)
   - Caching layer (Redis)

## Troubleshooting

### Database Connection Error
```
Error: FATAL: Ident authentication failed for user "postgres"
```
Solution: Check PostgreSQL credentials in application.yml

### Port Already in Use
```
Error: Address already in use
```
Solution: Change port in application.yml or kill existing process

### Maven Build Failure
```
mvn clean install -U
```

## Contributing
1. Create feature branch
2. Commit changes
3. Push to branch
4. Create Pull Request

## License
This project is licensed under the MIT License.

## Contact & Support
For issues, questions, or suggestions, please create an issue in the repository.

## Documentation References
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Swagger/OpenAPI](https://swagger.io/)
- [JWT (JSON Web Tokens)](https://jwt.io/)
