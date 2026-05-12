# SNL Banking - Complete Setup & Running Guide

## 🎯 Overview

This guide provides step-by-step instructions to set up and run the complete SNL Banking Application with both backend (Java Spring Boot) and frontend (React).

**Total Setup Time**: ~15-20 minutes
**Prerequisites**: Java 17, Node.js 14+, PostgreSQL 12+, Git

## 📋 Prerequisites Check

Before starting, verify you have all required software:

```bash
# Check Java version (should be 17+)
java -version

# Check Maven version (should be 3.6+)
mvn -version

# Check Node.js version (should be 14+)
node -v

# Check npm version (should be 6+)
npm -v

# Check PostgreSQL version (should be 12+)
psql --version
```

## 🗄️ Step 1: Database Setup

### 1.1 Start PostgreSQL Service

**On Windows:**
```bash
# PostgreSQL should be running. If not:
net start PostgreSQL-15
```

**On Mac:**
```bash
brew services start postgresql
```

**On Linux:**
```bash
sudo service postgresql start
```

### 1.2 Create Database and User

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE snl_banking;

# Create user (optional, you can use postgres)
CREATE USER snl_user WITH PASSWORD 'snl_password@123';
GRANT ALL PRIVILEGES ON DATABASE snl_banking TO snl_user;

# List databases to verify
\l

# Exit psql
\q
```

### 1.3 Verify Database Connection

```bash
# Test connection with postgres user
psql -U postgres -d snl_banking -c "SELECT version();"

# Or with custom user
psql -U snl_user -d snl_banking -c "SELECT version();"
```

## 🔙 Step 2: Backend Setup (Java/Spring Boot)

### 2.1 Navigate to Backend Directory

```bash
cd SNLBanking/backend
```

### 2.2 Configure Database Connection

**File**: `src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/snl_banking
    username: postgres        # Change if using snl_user
    password: postgres        # Change if using snl_password@123
    driver-class-name: org.postgresql.Driver
```

### 2.3 Build Backend

```bash
# Clean build with dependencies
mvn clean install

# This will:
# - Download dependencies
# - Compile Java code
# - Run unit tests
# - Create JAR file
```

**Expected Time**: 2-5 minutes (first build may take longer)

### 2.4 Verify Build Success

You should see:
```
[INFO] BUILD SUCCESS
[INFO] Total time: X.XXs
```

Generated JAR location:
```
target/snl-banking-app-1.0.0-SNAPSHOT.jar
```

### 2.5 Start Backend Server

**Option A: Using Maven**
```bash
mvn spring-boot:run
```

**Option B: Using JAR file**
```bash
java -jar target/snl-banking-app-1.0.0-SNAPSHOT.jar
```

### 2.6 Verify Backend is Running

Check these in your browser:

1. **Health Check**
   ```
   http://localhost:8080/api/actuator/health
   ```
   Should return: `{"status":"UP"}`

2. **Swagger UI**
   ```
   http://localhost:8080/api/swagger-ui.html
   ```
   Should show API documentation

3. **Database Connections**
   ```bash
   psql -U postgres -d snl_banking -c "\dt"
   ```
   Should list 6 tables: users, accounts, transactions, loans, bill_payments, audit_logs

### 2.7 Backend Terminal

Keep the backend terminal running. You should see:
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

Started SNLBankingApplication in X.XXX seconds
```

## 🎨 Step 3: Frontend Setup (React)

### 3.1 Open New Terminal/Command Prompt

Keep backend terminal running in one terminal. Open a new terminal for frontend.

### 3.2 Navigate to Frontend Directory

```bash
cd SNLBanking/frontend
```

### 3.3 Install Dependencies

```bash
npm install

# This will:
# - Read package.json
# - Download all dependencies
# - Create node_modules folder
```

**Expected Time**: 2-3 minutes

### 3.4 Configure Environment

Verify `.env` file exists with correct content:

**File**: `.env`
```env
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_APP_NAME=SNL Banking
REACT_APP_VERSION=1.0.0
```

### 3.5 Start Frontend Development Server

```bash
npm start

# This will:
# - Start development server on http://localhost:3000
# - Open browser automatically
# - Enable hot reload
```

**Expected Output**:
```
Compiled successfully!

You can now view snl-banking-frontend in the browser.

  Local:            http://localhost:3000
```

## ✅ Step 4: Verify Complete Setup

### 4.1 Check Both Servers Running

**Terminal 1 (Backend):**
- Should show: "Started SNLBankingApplication"
- Running on: http://localhost:8080

**Terminal 2 (Frontend):**
- Should show: "Compiled successfully!"
- Running on: http://localhost:3000

### 4.2 Test in Browser

1. **Open Frontend**
   ```
   http://localhost:3000
   ```

2. **You should see**
   - Login page with SNL Bank branding
   - Registration link

3. **Backend Status**
   ```
   http://localhost:8080/api/actuator/health
   ```
   Should show: `{"status":"UP"}`

4. **API Documentation**
   ```
   http://localhost:8080/api/swagger-ui.html
   ```
   Should show all endpoints

## 🧪 Step 5: Test the Application

### 5.1 Register a New User

**In Frontend (http://localhost:3000):**

1. Click "Register Now"
2. Fill form:
   - First Name: John
   - Last Name: Doe
   - Email: john@example.com
   - Mobile: 9876543210
   - PAN: ABCDE0001A
   - Aadhar: 123456789012
   - Address: 123 Main St
   - City: Mumbai
   - State: Maharashtra
   - Pincode: 400001
   - Password: Password@123

3. Click "Create Account"
4. See success message
5. Click "Go to Login"

### 5.2 Login

1. Email: john@example.com
2. Password: Password@123
3. Click "Login"

Should be redirected to dashboard.

### 5.3 Test Features

**Create Account:**
- Navigate to "Accounts" → "Create Account"
- Select account type: SAVINGS
- IFSC Code: HDFC0000001
- Click "Create Account"

**Transfer Funds:**
- Navigate to "Transfers"
- Use test data from test database (V2__Insert_Test_Data.sql)
- Try transferring between test accounts

**View Transactions:**
- Navigate to "Transactions"
- Select account from dropdown
- View transaction history

## 🗄️ Test Data Available

If you ran the database migration scripts, test data is available:

**Test Users:**
```
Email: rajesh.kumar@gmail.com (ID: 1)
Email: priya.sharma@gmail.com (ID: 2)
Email: admin@snlbanking.com (ID: 3)
```

**Test Accounts:**
```
Account 1: SAVINGS, Balance: 10000.00
Account 2: CURRENT, Balance: 50000.00
Account 3: SALARY, Balance: 100000.00
Account 4: SAVINGS, Balance: 25000.00
```

**Test Transactions:**
```
3 completed transactions between test accounts
```

## 🛑 Troubleshooting

### Problem: Backend Won't Start

**Error**: `Port 8080 already in use`
```bash
# Find process on port 8080
lsof -ti:8080 | xargs kill -9

# Windows:
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

**Error**: `Database connection failed`
```bash
# Verify PostgreSQL is running
psql -U postgres -c "SELECT 1;"

# Check database exists
psql -U postgres -c "\l" | grep snl_banking
```

### Problem: Frontend Won't Start

**Error**: `Port 3000 already in use`
```bash
# Find process on port 3000
lsof -ti:3000 | xargs kill -9

# Windows:
netstat -ano | findstr :3000
taskkill /PID <PID> /F
```

**Error**: `npm ERR! ERESOLVE unable to resolve dependency tree`
```bash
npm install --legacy-peer-deps
```

### Problem: Cannot Connect to Backend

**Error**: Network Error in Frontend
```
1. Check backend is running (http://localhost:8080/api/actuator/health)
2. Check .env file has correct API_URL
3. Check CORS is enabled in backend
4. Check firewall settings
```

**In browser DevTools → Network:**
- Check failed requests
- Check response status codes
- Look for CORS errors

## 📊 Useful Commands

### Backend Commands

```bash
# Navigate to backend
cd SNLBanking/backend

# Build project
mvn clean install

# Run tests
mvn test

# Run with Spring Boot plugin
mvn spring-boot:run

# Generate code coverage report
mvn clean test jacoco:report

# View logs
tail -f logs/snl-banking.log
```

### Frontend Commands

```bash
# Navigate to frontend
cd SNLBanking/frontend

# Install dependencies
npm install

# Start development server
npm start

# Build for production
npm run build

# Run tests
npm test

# Eject (be careful!)
npm run eject
```

### Database Commands

```bash
# Connect to database
psql -U postgres -d snl_banking

# List all tables
\dt

# View user table
SELECT * FROM users;

# View accounts table
SELECT * FROM accounts;

# View transactions table
SELECT * FROM transactions;

# Create backup
pg_dump -U postgres snl_banking > backup.sql

# Restore backup
psql -U postgres snl_banking < backup.sql

# Exit psql
\q
```

## 🔄 Complete Workflow Summary

```
┌─────────────────────────────────────────────────────────────┐
│ 1. Start PostgreSQL Database                               │
│    psql -U postgres                                        │
│    CREATE DATABASE snl_banking;                            │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 2. Terminal 1: Start Backend Server                         │
│    cd SNLBanking/backend                                   │
│    mvn clean install                                       │
│    mvn spring-boot:run                                     │
│    Verify: http://localhost:8080/api/actuator/health      │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 3. Terminal 2: Start Frontend Server                        │
│    cd SNLBanking/frontend                                  │
│    npm install                                             │
│    npm start                                               │
│    Verify: http://localhost:3000                           │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 4. Test the Application                                     │
│    Register → Login → Create Account → Transfer            │
│    Check Dashboard, Transactions, Profile                  │
└─────────────────────────────────────────────────────────────┘
```

## 📚 Documentation References

### Backend Documentation
- [Backend README](./backend/README.md)
- [Development Summary](./DEVELOPMENT_SUMMARY.md)
- [API Swagger](http://localhost:8080/api/swagger-ui.html)

### Frontend Documentation
- [Frontend README](./frontend/FRONTEND_README.md)
- [Frontend Development Summary](./frontend/FRONTEND_DEVELOPMENT_SUMMARY.md)

### Main Documentation
- [Main README](./README.md)
- [Quick Start Guide](./QUICK_START.md)

## 🎓 Next Steps After Setup

1. **Explore the Code**
   - Review backend architecture in `backend/src`
   - Review frontend components in `frontend/src`

2. **Understand the Flow**
   - User registration → Backend API → Database
   - Login → JWT Token → Redux Store
   - Fund Transfer → Backend Logic → Database Update

3. **Customize**
   - Change app colors in `frontend/src/theme/theme.js`
   - Add new pages in `frontend/src/pages/`
   - Add new endpoints in backend controllers

4. **Deploy**
   - Backend: Docker + AWS/Azure
   - Frontend: Netlify/Vercel
   - Database: AWS RDS/Azure Database

## 🆘 Getting Help

### Check Logs

**Backend Logs:**
```bash
cd SNLBanking/backend
tail -f logs/snl-banking.log
```

**Frontend Console:**
- Open Browser DevTools (F12)
- Go to Console tab
- Look for errors

### Database Issues

```bash
# Check database exists
psql -U postgres -l

# Check tables created
psql -U postgres -d snl_banking -c "\dt"

# Check data in tables
psql -U postgres -d snl_banking -c "SELECT COUNT(*) FROM users;"
```

### Port Issues

```bash
# Find what's running on port 8080
lsof -i :8080

# Find what's running on port 3000
lsof -i :3000

# Find what's running on port 5432
lsof -i :5432
```

## 💡 Performance Tips

1. **Backend Performance**
   - Use PostgreSQL indexes (created automatically)
   - Connection pooling with HikariCP
   - Caching strategies can be added

2. **Frontend Performance**
   - React Fast Refresh for development
   - Code splitting for production
   - Lazy loading of routes can be added

3. **Database Performance**
   - Run VACUUM ANALYZE periodically
   - Monitor slow queries
   - Use EXPLAIN for query analysis

## ✨ Features Ready to Use

✅ User Authentication (Login/Register)
✅ Account Management (Create, View, Freeze)
✅ Fund Transfers (Intra-bank)
✅ Transaction History (View, Filter)
✅ User Dashboard (Overview)
✅ User Profile (View/Edit)
✅ Responsive Design (Mobile/Tablet/Desktop)
✅ Error Handling & Notifications
✅ Form Validation
✅ Protected Routes

## 🔐 Security Notes

1. **Change default passwords** in production
2. **Use environment variables** for secrets
3. **Enable HTTPS** for production
4. **Implement rate limiting** for APIs
5. **Add CSRF protection** for forms
6. **Use stronger JWT secret** in production

## 📞 Support

If you encounter issues:

1. Check the logs (backend logs, browser console)
2. Verify all prerequisites are installed
3. Check database connection
4. Review API endpoints in Swagger UI
5. Test API endpoints manually with cURL or Postman
6. Check Redux state in DevTools

---

**Congratulations!** 🎉

Your SNL Banking Application is now set up and running. You have a fully functional banking system with a modern frontend and robust backend.

Happy coding! 💻
