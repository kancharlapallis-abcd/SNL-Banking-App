# SNL Banking Frontend - React Application

A modern, responsive banking application built with React.js, Redux, and Material-UI. This frontend provides a complete user interface for core banking operations including account management, fund transfers, and transaction history.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)
- [API Integration](#api-integration)
- [State Management](#state-management)
- [Component Overview](#component-overview)
- [Routing](#routing)
- [Styling & Theme](#styling--theme)
- [Form Validation](#form-validation)
- [Error Handling](#error-handling)
- [Best Practices](#best-practices)
- [Phase 1 Features](#phase-1-features)
- [Phase 2 & 3 Roadmap](#phase-2--3-roadmap)

## ✨ Features

### Phase 1: Core Banking (Implemented)
✅ **User Authentication**
- Secure login and registration
- Password validation and strength checking
- Session management with JWT tokens
- Role-based access control (CUSTOMER, ADMIN, SUPPORT_STAFF)

✅ **Account Management**
- View all accounts
- Create new accounts (Savings, Current, Salary)
- View account details and balance
- Freeze/Unfreeze accounts
- Account statements

✅ **Fund Transfers**
- Intra-bank transfers
- Real-time fund transfer with validation
- Transaction reference tracking
- Transfer history and status monitoring

✅ **Transaction Management**
- Transaction history with filters
- Transaction details and status
- Transaction reversal (for PENDING status)
- Export transaction statements

✅ **User Dashboard**
- Account summary cards
- Quick action buttons
- Recent transaction feed
- Account balance overview

### Phase 2: Enhanced Services (In Development)
🔜 Bill Payments
🔜 Loan Applications & Management
🔜 Customer Support Integration

### Phase 3: Advanced Features (Planned)
🔜 Investment Services
🔜 Personalized Financial Management (PFM)
🔜 Mobile Check Deposit

## 🛠 Tech Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| React | 18.2.0 | UI Library |
| React Router | 6.14.0 | Client-side routing |
| Redux Toolkit | 1.9.5 | State management |
| Material-UI | 5.13.5 | Component library & styling |
| Axios | 1.4.0 | HTTP client |
| Formik | 2.4.2 | Form management |
| Yup | 1.2.0 | Schema validation |
| React Toastify | 9.1.3 | Notifications |
| Date-fns | 2.30.0 | Date manipulation |

## 📁 Project Structure

```
frontend/
├── src/
│   ├── components/
│   │   └── ProtectedRoute.jsx          # Route protection wrapper
│   │
│   ├── pages/
│   │   ├── LoginPage.jsx               # User login
│   │   ├── RegisterPage.jsx            # User registration
│   │   ├── DashboardPage.jsx           # Dashboard & overview
│   │   ├── AccountsPage.jsx            # Accounts list
│   │   ├── AccountDetailPage.jsx       # Account details
│   │   ├── CreateAccountPage.jsx       # Create new account
│   │   ├── TransferFundsPage.jsx       # Fund transfer
│   │   ├── TransactionsPage.jsx        # Transaction history
│   │   ├── ProfilePage.jsx             # User profile
│   │   └── NotFoundPage.jsx            # 404 page
│   │
│   ├── services/
│   │   ├── apiClient.js                # Axios instance with interceptors
│   │   ├── authService.js              # Authentication API calls
│   │   ├── userService.js              # User API calls
│   │   ├── accountService.js           # Account API calls
│   │   └── transactionService.js       # Transaction API calls
│   │
│   ├── store/
│   │   ├── store.js                    # Redux store configuration
│   │   ├── authSlice.js                # Auth state management
│   │   ├── accountSlice.js             # Account state management
│   │   └── transactionSlice.js         # Transaction state management
│   │
│   ├── layouts/
│   │   ├── Header.jsx                  # App header/navbar
│   │   └── MainLayout.jsx              # Main layout wrapper
│   │
│   ├── theme/
│   │   └── theme.js                    # Material-UI theme customization
│   │
│   ├── hooks/
│   │   └── useCustomHooks.js           # Custom React hooks
│   │
│   ├── utils/
│   │   └── formatters.js               # Helper functions (formatting, validation)
│   │
│   ├── constants/
│   │   └── apiEndpoints.js             # API endpoint definitions
│   │
│   ├── App.jsx                         # Main app component with routing
│   ├── index.jsx                       # React app entry point
│   └── index.css                       # Global styles
│
├── public/
│   └── index.html                      # HTML template
│
├── .env                                # Environment variables
├── package.json                        # Dependencies & scripts
└── README.md                           # This file
```

## ⚙️ Installation & Setup

### Prerequisites
- **Node.js**: v14.0 or higher
- **npm**: v6.0 or higher
- **Git**: For cloning the repository

### Step 1: Clone and Navigate
```bash
cd SNLBanking/frontend
```

### Step 2: Install Dependencies
```bash
npm install
```

### Step 3: Configure Environment
Create/update `.env` file:
```env
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_APP_NAME=SNL Banking
REACT_APP_VERSION=1.0.0
```

### Step 4: Start Development Server
```bash
npm start
```

The application will open at `http://localhost:3000`

## 🚀 Running the Application

### Development Mode
```bash
npm start
```
- Hot reload enabled
- Development tools available
- Runs on `http://localhost:3000`

### Production Build
```bash
npm run build
```
- Creates optimized production build
- Output in `build/` directory
- Ready for deployment

### Run Tests
```bash
npm test
```
- Runs test suite
- Watch mode enabled

## 🔌 API Integration

### API Client Setup
The frontend uses Axios for HTTP requests with automatic JWT token management.

**File**: `src/services/apiClient.js`

#### Features:
- Automatic JWT token attachment to requests
- Request/response interceptors
- Token expiration handling
- Automatic redirect to login on 401 errors

#### Example API Call:
```javascript
// From authService.js
const response = await apiClient.post('/v1/users/register', userData);
```

### API Services

#### authService.js
```javascript
- register(userData)        // User registration
- login(email, password)    // User login
- logout()                  // Clear session
- changePassword(...)       // Change password
- resetPassword(email)      // Reset password
```

#### userService.js
```javascript
- getUserById(userId)       // Get user details
- updateUserProfile(...)    // Update profile
- getAllUsers(page, size)   // Get all users (Admin)
```

#### accountService.js
```javascript
- createAccount(data)       // Create account
- getUserAccounts(userId)   // Get user's accounts
- getAccountBalance(id)     // Get account balance
- freezeAccount(id)         // Freeze account
- unfreezeAccount(id)       // Unfreeze account
```

#### transactionService.js
```javascript
- transferFunds(data)       // Transfer funds
- getAccountTransactions()  // Get transactions
- getTransactionHistory()   // Get history
- reverseTransaction(id)    // Reverse transaction
```

## 🏪 State Management

### Redux Store Architecture

**Store Location**: `src/store/store.js`

```
Redux Store
├── auth slice (authSlice.js)
│   ├── user
│   ├── token
│   ├── isAuthenticated
│   ├── loading
│   └── error
│
├── account slice (accountSlice.js)
│   ├── accounts
│   ├── selectedAccount
│   ├── loading
│   └── error
│
└── transaction slice (transactionSlice.js)
    ├── transactions
    ├── selectedTransaction
    ├── loading
    └── error
```

### Using Redux (Example)
```javascript
// In a component
import { useDispatch, useSelector } from 'react-redux';
import { getUserAccounts } from '../store/accountSlice';

function MyComponent() {
  const dispatch = useDispatch();
  const { accounts, loading } = useSelector(state => state.account);
  
  useEffect(() => {
    dispatch(getUserAccounts(userId));
  }, []);
  
  return (
    // Render accounts...
  );
}
```

### Custom Hooks
```javascript
import { useAuth, useAccounts, useTransactions } from '../hooks/useCustomHooks';

// In component
const { user, isAuthenticated, logout } = useAuth();
const { accounts, loading } = useAccounts();
const { transactions } = useTransactions();
```

## 🧩 Component Overview

### Layout Components

#### Header.jsx
- Navigation bar
- User menu dropdown
- Logout functionality
- Responsive design

#### MainLayout.jsx
- Wraps all protected pages
- Consistent layout structure
- Header integration

### Page Components

#### LoginPage.jsx
- Email & password input
- Form validation with Formik & Yup
- Error handling
- Link to registration

#### RegisterPage.jsx
- Multi-step form
- Personal information
- Address details
- Password confirmation
- State selection dropdown

#### DashboardPage.jsx
- Account summary cards
- Total balance overview
- Active accounts count
- Recent transactions
- Quick action buttons
- Account list preview

#### AccountsPage.jsx
- List all user accounts
- Account type color coding
- Balance display
- Freeze/Unfreeze functionality
- Create account button
- Responsive grid layout

#### AccountDetailPage.jsx
- Complete account information
- IFSC & MICR codes
- Account creation date
- Recent transactions list
- Account action buttons

#### CreateAccountPage.jsx
- Account type selection
- IFSC code input
- MICR code input
- Account type descriptions
- Form validation

#### TransferFundsPage.jsx
- From account selection
- Recipient account number
- Amount input
- Description (optional)
- Transaction charge info
- Quick tips section

#### TransactionsPage.jsx
- Account filter dropdown
- Transaction table
- Date & amount display
- Transaction status chips
- Pagination support
- Reference number display

#### ProfilePage.jsx
- User avatar with initials
- Profile information display
- Editable profile fields
- Address and contact info
- Change password option

## 🛣 Routing

### Public Routes
- `/login` - Login page
- `/register` - Registration page

### Protected Routes
- `/dashboard` - Main dashboard
- `/accounts` - Accounts list
- `/accounts/:accountId` - Account details
- `/create-account` - Create account
- `/transfers` - Fund transfer
- `/transactions` - Transaction history
- `/profile` - User profile

### Route Protection
Uses `ProtectedRoute` wrapper component:
```javascript
<Route
  path="/dashboard"
  element={
    <ProtectedRoute>
      <DashboardPage />
    </ProtectedRoute>
  }
/>
```

## 🎨 Styling & Theme

### Material-UI Theme
**File**: `src/theme/theme.js`

#### Color Palette
- **Primary**: #1F3A93 (HDFC Blue)
- **Secondary**: #FFA500 (Orange accent)
- **Success**: #4CAF50
- **Error**: #F44336
- **Warning**: #FF9800

#### Typography
- **H1-H6**: Custom sizes with primary color
- **Body**: Roboto font family
- **Buttons**: Custom elevation and hover effects

#### Custom Component Styles
- Buttons with custom padding and border radius
- Cards with subtle shadows
- Text fields with primary color borders
- App bar with primary background

### Global Styles
**File**: `src/index.css`
- Reset margins and padding
- Custom scrollbar styling
- Link styling
- Input autofill styling

## ✅ Form Validation

### Formik + Yup Integration
**File**: `src/pages/LoginPage.jsx`

```javascript
const validationSchema = yup.object({
  email: yup
    .string('Enter your email')
    .email('Enter a valid email')
    .required('Email is required'),
  password: yup
    .string('Enter your password')
    .required('Password is required'),
});
```

### Validation Rules

#### User Validation
```javascript
- Email: Valid email format (^[A-Za-z0-9+_.-]+@(.+)$)
- Mobile: 10 digits (^\d{10}$)
- PAN: AAAAA0001A format
- Aadhar: 12 digits (^\d{12}$)
- Pincode: 6 digits (^\d{6}$)
- Password: Min 8 characters
```

#### Account Validation
```javascript
- IFSC: Valid IFSC format (^[A-Z]{4}0[A-Z0-9]{6}$)
- Account Type: SAVINGS, CURRENT, or SALARY
```

#### Transaction Validation
```javascript
- Amount: Positive number
- Account Number: Valid format
- Description: Optional, max 255 chars
```

## ⚠️ Error Handling

### Global Error Handling
- Redux error state in each slice
- Error messages displayed via Toast notifications
- API interceptors for 401/500 errors
- User-friendly error messages

### Toast Notifications
```javascript
import { ToastContainer, toast } from 'react-toastify';

// In component
toast.success('Action successful');
toast.error('An error occurred');
toast.info('Information message');
toast.warning('Warning message');

// In JSX
<ToastContainer position="bottom-right" autoClose={3000} />
```

## 📋 Best Practices

### Code Organization
- ✅ Separation of concerns (services, components, pages)
- ✅ Reusable components
- ✅ Custom hooks for logic abstraction
- ✅ Redux for global state management
- ✅ Constants for API endpoints

### Security
- ✅ JWT token stored in localStorage
- ✅ Automatic token inclusion in requests
- ✅ Token expiration handling
- ✅ Protected routes with ProtectedRoute component
- ✅ Password validation on client-side

### Performance
- ✅ Code splitting with React.lazy (can be added)
- ✅ Memoization with useMemo/useCallback (where needed)
- ✅ Efficient re-renders with Redux
- ✅ Optimized images and assets
- ✅ Pagination for large datasets

### UX/Accessibility
- ✅ Responsive design (Mobile, Tablet, Desktop)
- ✅ Loading states and spinners
- ✅ Error messages and validation feedback
- ✅ Keyboard navigation support
- ✅ Color contrast compliance
- ✅ ARIA labels for screen readers

## 📱 Phase 1 Features Summary

| Feature | Status | Pages | APIs |
|---------|--------|-------|------|
| User Authentication | ✅ | Login, Register | POST /users/register |
| Account Management | ✅ | Accounts, Create, Detail | GET/POST /accounts |
| Fund Transfers | ✅ | Transfer | POST /transactions/transfer |
| Transaction History | ✅ | Transactions | GET /transactions |
| User Profile | ✅ | Profile | GET/PUT /users |
| Dashboard | ✅ | Dashboard | Multiple endpoints |

## 🔜 Phase 2 & 3 Roadmap

### Phase 2: Enhanced Services
```
- Bill Payments
  ├── Pay bills (Electricity, Water, Mobile, etc.)
  ├── Recurring bill setup
  └── Bill payment history

- Loan Management
  ├── Loan application form
  ├── Loan status tracking
  ├── EMI calculation
  └── Loan statement download

- Customer Support
  ├── Ticket creation
  ├── Live chat integration
  └── Support ticket history
```

### Phase 3: Advanced Features
```
- Investment Services
  ├── Mutual fund investment
  ├── Stock trading (via integration)
  └── Investment portfolio

- Personalized Financial Management
  ├── Budget tracking
  ├── Spending analysis
  ├── Financial insights
  └── Expense categorization

- Mobile Check Deposit
  ├── Image upload
  ├── Check processing
  └── Deposit history
```

## 📝 Environment Variables

```env
# API Configuration
REACT_APP_API_URL=http://localhost:8080/api

# App Configuration
REACT_APP_APP_NAME=SNL Banking
REACT_APP_VERSION=1.0.0
```

## 🤝 Contributing

### Development Workflow
1. Create feature branch: `git checkout -b feature/feature-name`
2. Make changes and test
3. Commit changes: `git commit -m "Add feature"`
4. Push to branch: `git push origin feature/feature-name`
5. Submit pull request

### Code Standards
- Use functional components with hooks
- Follow Redux patterns for state management
- Add error handling for all API calls
- Test components before committing
- Keep components focused and reusable

## 📞 Support & Troubleshooting

### Common Issues

**Issue**: Port 3000 already in use
```bash
# Kill process on port 3000
lsof -ti:3000 | xargs kill -9
```

**Issue**: npm dependencies conflict
```bash
npm install --legacy-peer-deps
```

**Issue**: API connection errors
- Check backend is running on `http://localhost:8080`
- Verify `.env` file has correct `REACT_APP_API_URL`
- Check network requests in browser DevTools

### Getting Help
1. Check component README sections
2. Review Redux store structure
3. Check browser console for errors
4. Verify API endpoints in DevTools Network tab

## 📄 License

This project is part of the SNL Banking Application and follows the same license as the main project.

---

**Last Updated**: May 2026
**Version**: 1.0.0
**Status**: Phase 1 Complete, Phase 2 In Development
