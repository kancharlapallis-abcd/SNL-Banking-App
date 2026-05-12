# SNL Banking Application - Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Prerequisites Check
```bash
java -version    # Should be 17+
mvn -version     # Should be 3.6+
psql --version   # Should be PostgreSQL 12+
node -v          # Should be 14+
```

## Backend Setup (Java/Spring Boot)

### Step 1: Database Setup
```bash
# Login to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE snl_banking;

# Create user
CREATE USER snl_user WITH PASSWORD 'snl_password';
GRANT ALL PRIVILEGES ON DATABASE snl_banking TO snl_user;
ALTER DATABASE snl_banking OWNER TO snl_user;

# Exit
\q
```

### Step 2: Configure Backend
```bash
cd SNLBanking/backend

# Update application.yml if needed
# File: src/main/resources/application.yml
# Change username: postgres to snl_user (if you created custom user)
# Change password: postgres to snl_password (if you created custom user)
```

### Step 3: Build & Run
```bash
# Clean build
mvn clean install

# Run application
mvn spring-boot:run

# Or compile and run JAR
mvn clean package
java -jar target/snl-banking-app-1.0.0-SNAPSHOT.jar
```

### Step 4: Verify Backend
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **Health Check**: http://localhost:8080/api/actuator/health
- **API Base**: http://localhost:8080/api

## Frontend Setup (React)

### Step 1: Install Dependencies
```bash
cd SNLBanking/frontend
npm install
```

### Step 2: Configure Environment
```bash
# Create .env file
echo "REACT_APP_API_URL=http://localhost:8080/api" > .env
```

### Step 3: Start Development Server
```bash
npm start
# Will open http://localhost:3000
```

## Test the Application

### Using Swagger UI
1. Open http://localhost:8080/api/swagger-ui.html
2. Navigate to User Management section
3. Try "POST /v1/users/register"
4. Use this test data:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "Password@123",
  "mobileNumber": "9876543210",
  "panNumber": "AAAAA0001A",
  "aadharNumber": "123456789012"
}
```

### Using cURL
```bash
# Register User
curl -X POST http://localhost:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "password": "Password@123",
    "mobileNumber": "9876543210",
    "panNumber": "AAAAA0001A",
    "aadharNumber": "123456789012"
  }'

# Get User
curl -X GET http://localhost:8080/api/v1/users/1
```

## Important Files to Know

### Backend Configuration
- **Main App**: `backend/src/main/java/com/snlbanking/SNLBankingApplication.java`
- **Config**: `backend/src/main/resources/application.yml`
- **Database**: `backend/src/main/resources/db/migration/`

### Key Endpoints

**User Management**
- `POST /v1/users/register` - Register user
- `GET /v1/users/{id}` - Get user
- `PUT /v1/users/{id}` - Update user
- `POST /v1/users/{id}/activate` - Activate user
- `POST /v1/users/{id}/change-password` - Change password

**Account Management**
- `POST /v1/accounts` - Create account
- `GET /v1/accounts/{id}` - Get account
- `GET /v1/accounts/{id}/balance` - Get balance
- `GET /v1/accounts/user/{userId}` - Get user accounts
- `POST /v1/accounts/{id}/freeze` - Freeze account

**Transactions**
- `POST /v1/transactions/transfer` - Transfer funds
- `GET /v1/transactions/account/{accountId}` - Get transactions
- `GET /v1/transactions/{id}` - Get transaction details

## Sample Test Data

Once the app is running, test with these credentials:

**Test User 1** (Already inserted)
- Email: rajesh.kumar@gmail.com
- User ID: 1
- Account ID: 1

**Test User 2** (Already inserted)
- Email: priya.sharma@gmail.com
- User ID: 2
- Account ID: 3

**Admin User**
- Email: admin@snlbanking.com
- User ID: 3

## Common Commands

### Run Tests
```bash
cd SNLBanking/backend
mvn test                    # Run all tests
mvn test -Dtest=UserServiceTest    # Run specific test
mvn test jacoco:report     # Generate coverage report
```

### View Logs
```bash
# Logs are in backend/logs/ directory
tail -f logs/snl-banking.log
```

### Database Access
```bash
# Connect to database
psql -U snl_user -d snl_banking

# Useful commands
\dt                    # List tables
SELECT * FROM users;   # View users
SELECT * FROM accounts; # View accounts
```

### Stop Application
```bash
# Press Ctrl+C in terminal
# Or kill the process
kill -9 $(lsof -t -i:8080)
```

## Troubleshooting

### Database Connection Failed
```
Error: Could not get a connection
Solution: Ensure PostgreSQL is running
sudo service postgresql start  # Linux/Mac
```

### Port 8080 Already in Use
```
Error: Address already in use
Solution: Kill the process on port 8080
lsof -ti:8080 | xargs kill -9
```

### Maven Build Failed
```
Error: Failed to execute goal
Solution: Update dependencies
mvn clean install -U
```

### npm install Failed
```
Error: npm ERR! ERESOLVE unable to resolve dependency tree
Solution: Use legacy peer deps
npm install --legacy-peer-deps
```

## Next Steps

1. **Create UI Components**
   - Login page
   - Dashboard
   - Account management page
   - Transaction page

2. **Implement Authentication**
   - JWT token integration
   - Login functionality
   - Protected routes

3. **Connect Frontend to Backend**
   - API service integration
   - Redux state management
   - Form handling

4. **Add Advanced Features**
   - Bill payments
   - Loan management
   - Transaction history with filters
   - Account statements

5. **Deploy Application**
   - Docker containerization
   - CI/CD pipeline setup
   - Production deployment

## Useful Resources

- **API Documentation**: http://localhost:8080/api/swagger-ui.html
- **Backend README**: `SNLBanking/backend/README.md`
- **Frontend README**: `SNLBanking/frontend/README.md`
- **Development Summary**: `SNLBanking/DEVELOPMENT_SUMMARY.md`
- **Root Documentation**: `SNLBanking/README.md`

## Support

For issues or questions:
1. Check the README files
2. Review the source code comments
3. Check Swagger UI for API details
4. Review test files for usage examples

---

**Ready to Code!** 🎉

You have a fully functional banking application framework with:
- ✅ Complete Java/Spring Boot backend
- ✅ 6 database entities
- ✅ 15+ REST API endpoints
- ✅ Unit tests
- ✅ Database migrations
- ✅ React frontend structure
- ✅ Comprehensive documentation

Start building your features! 💪
