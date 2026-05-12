# SNL Banking Frontend - React Application

## Project Overview
React-based frontend for SNL Banking Application with modern UI/UX design following HDFC banking patterns.

## Technology Stack
- **Framework**: React 18.2
- **Routing**: React Router v6
- **State Management**: Redux Toolkit
- **UI Library**: Material-UI (MUI)
- **API Client**: Axios
- **Form Handling**: Formik + Yup
- **Testing**: Jest + React Testing Library
- **CSS**: Emotion (MUI styled)

## Prerequisites
- Node.js 14+ and npm 6+
- React development knowledge
- Familiarity with Redux and Material-UI

## Installation

```bash
# Navigate to frontend directory
cd SNLBanking/frontend

# Install dependencies
npm install

# Start development server
npm start

# Build for production
npm build

# Run tests
npm test
```

## Project Structure
```
frontend/
├── src/
│   ├── components/
│   │   ├── Auth/                    # Authentication Components
│   │   │   ├── Login.jsx
│   │   │   ├── Register.jsx
│   │   │   └── PasswordReset.jsx
│   │   ├── Dashboard/               # Dashboard Components
│   │   │   ├── Dashboard.jsx
│   │   │   └── AccountOverview.jsx
│   │   ├── Account/                 # Account Components
│   │   │   ├── AccountList.jsx
│   │   │   ├── AccountDetail.jsx
│   │   │   └── CreateAccount.jsx
│   │   ├── Transactions/            # Transaction Components
│   │   │   ├── FundTransfer.jsx
│   │   │   ├── TransactionHistory.jsx
│   │   │   └── TransactionDetail.jsx
│   │   ├── Layout/                  # Layout Components
│   │   │   ├── Header.jsx
│   │   │   ├── Sidebar.jsx
│   │   │   └── Footer.jsx
│   │   └── Common/                  # Reusable Components
│   │       ├── Navbar.jsx
│   │       ├── Loading.jsx
│   │       └── ErrorBoundary.jsx
│   ├── pages/
│   │   ├── Home.jsx
│   │   ├── NotFound.jsx
│   │   └── Unauthorized.jsx
│   ├── redux/
│   │   ├── slices/
│   │   │   ├── authSlice.js
│   │   │   ├── userSlice.js
│   │   │   ├── accountSlice.js
│   │   │   └── transactionSlice.js
│   │   ├── store.js
│   │   └── hooks.js
│   ├── services/
│   │   ├── api.js                   # Axios instance
│   │   ├── authService.js           # Auth API calls
│   │   ├── userService.js           # User API calls
│   │   ├── accountService.js        # Account API calls
│   │   └── transactionService.js    # Transaction API calls
│   ├── hooks/
│   │   ├── useAuth.js
│   │   ├── useFetch.js
│   │   └── useForm.js
│   ├── styles/
│   │   ├── theme.js                 # MUI Theme
│   │   ├── global.css
│   │   └── variables.css
│   ├── utils/
│   │   ├── constants.js
│   │   ├── helpers.js
│   │   └── validation.js
│   ├── App.jsx
│   └── index.js
├── public/
│   ├── index.html
│   ├── favicon.ico
│   └── manifest.json
├── package.json
├── .env.example
└── README.md
```

## Key Features

### Authentication
- User Registration
- Login/Logout
- Password Reset
- Session Management
- JWT Token Handling

### Account Management
- View All Accounts
- Account Details
- Create New Account
- Close Account
- Freeze/Unfreeze Account

### Transactions
- Fund Transfers
- Transaction History
- Transaction Details
- Download Statements
- Transaction Reversal

### Dashboard
- Account Overview
- Recent Transactions
- Quick Actions
- Notifications

## API Integration

### Base Configuration
```javascript
// services/api.js
import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Add JWT token to requests
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
```

### Example API Service
```javascript
// services/userService.js
import api from './api';

export const userService = {
  register: (userData) => api.post('/v1/users/register', userData),
  getUser: (userId) => api.get(`/v1/users/${userId}`),
  updateProfile: (userId, userData) => api.put(`/v1/users/${userId}`, userData),
  changePassword: (userId, passwords) => 
    api.post(`/v1/users/${userId}/change-password`, null, { params: passwords })
};
```

## Redux State Management

### Auth Slice Example
```javascript
// redux/slices/authSlice.js
import { createSlice } from '@reduxjs/toolkit';

const authSlice = createSlice({
  name: 'auth',
  initialState: {
    user: null,
    token: localStorage.getItem('token'),
    isLoading: false,
    error: null
  },
  reducers: {
    loginSuccess: (state, action) => {
      state.user = action.payload.user;
      state.token = action.payload.token;
      state.isLoading = false;
      localStorage.setItem('token', action.payload.token);
    },
    logout: (state) => {
      state.user = null;
      state.token = null;
      localStorage.removeItem('token');
    }
  }
});

export default authSlice.reducer;
```

## Environment Variables

Create `.env` file in frontend directory:
```
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_JWT_SECRET=your-secret-key
REACT_APP_LOG_LEVEL=debug
```

## Component Examples

### Login Component
```jsx
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { loginSuccess } from '../redux/slices/authSlice';

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      // API call would go here
      dispatch(loginSuccess({ user: { email }, token: 'jwt-token' }));
      navigate('/dashboard');
    } catch (error) {
      console.error('Login failed:', error);
    }
  };

  return (
    <form onSubmit={handleLogin}>
      <input value={email} onChange={(e) => setEmail(e.target.value)} />
      <input value={password} onChange={(e) => setPassword(e.target.value)} />
      <button type="submit">Login</button>
    </form>
  );
}

export default Login;
```

## Available Scripts

### `npm start`
Runs the app in development mode
- Open [http://localhost:3000](http://localhost:3000) to view in browser
- Page reloads on code changes

### `npm build`
Builds the app for production in the `build` folder

### `npm test`
Launches the test runner in interactive watch mode

## Best Practices

1. **Component Structure**: Keep components small and focused
2. **State Management**: Use Redux for global state, useState for local state
3. **API Calls**: Use custom hooks for API calls
4. **Error Handling**: Implement error boundaries and user feedback
5. **Form Validation**: Use Formik + Yup for form validation
6. **Accessibility**: Follow WAI-ARIA guidelines
7. **Performance**: Use React.memo and useMemo for optimization
8. **Testing**: Write unit tests for components and services

## Styling with MUI

### Theme Customization
```javascript
// styles/theme.js
import { createTheme } from '@mui/material/styles';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
  typography: {
    fontFamily: 'Roboto, sans-serif',
  },
});

export default theme;
```

## Security Considerations

1. **JWT Storage**: Store tokens securely (localStorage/sessionStorage)
2. **HTTPS**: Use HTTPS in production
3. **CORS**: Configure CORS on backend
4. **Input Validation**: Validate all user inputs
5. **XSS Prevention**: Sanitize HTML/CSS
6. **CSRF Protection**: Implement CSRF tokens

## Deployment

### To Vercel
```bash
npm install -g vercel
vercel
```

### To Netlify
```bash
npm install -g netlify-cli
netlify deploy --prod --dir=build
```

### To Docker
```dockerfile
FROM node:18-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build
EXPOSE 3000
CMD ["npm", "start"]
```

## Troubleshooting

### CORS Errors
- Ensure backend has CORS enabled
- Check API_URL in environment variables

### Blank Page
- Check browser console for errors
- Verify API is running on correct port

### Authentication Issues
- Clear localStorage and try again
- Check JWT token expiration

## Future Enhancements
- PWA support
- Offline functionality
- Mobile app using React Native
- Dark mode support
- Multi-language support
- Advanced analytics dashboard

## Support
For issues or questions, create an issue in the repository.

## License
MIT License - See LICENSE file for details
