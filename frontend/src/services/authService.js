
import apiClient from './apiClient';
import { AUTH_ENDPOINTS, USER_ENDPOINTS } from '../constants/apiEndpoints';

const authService = {
  // Register new user
  register: async (userData) => {
    try {
      const response = await apiClient.post(AUTH_ENDPOINTS.REGISTER, userData);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Real Login Implementation
  login: async (email, password) => {
    try {
      // Use the constant for /v1/auth/login
      const response = await apiClient.post(AUTH_ENDPOINTS.LOGIN, { email, password });
      
      const result = response.data.data || response.data; 
      
      if (result.token) {
    localStorage.setItem('token', result.token);
    // REMOVE .user -> Just stringify the result itself
    localStorage.setItem('user', JSON.stringify(result)); 
    return result; 
}
      
      throw new Error('Invalid response from server: Token missing');
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Logout - Clears both keys to be safe
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('authToken'); 
  },

  // Get current user
  getCurrentUser: () => {
    const user = localStorage.getItem('user');
    try {
      return user ? JSON.parse(user) : null;
    } catch (e) {
      return null;
    }
  },

  // Check if user is authenticated - Fixed to look for 'token'
  isAuthenticated: () => {
    return !!localStorage.getItem('token');
  },

  // Get auth token - Fixed to look for 'token'
  getToken: () => {
    return localStorage.getItem('token');
  },

  // Change password
  changePassword: async (userId, oldPassword, newPassword) => {
    try {
      const response = await apiClient.post(
        USER_ENDPOINTS.CHANGE_PASSWORD(userId),
        { oldPassword, newPassword }
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Reset password
  resetPassword: async (email) => {
    try {
      const response = await apiClient.post(USER_ENDPOINTS.RESET_PASSWORD, { email });
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },
};

export default authService;

// import apiClient from './apiClient';
// import { USER_ENDPOINTS } from '../constants/apiEndpoints';

// // Auth Service for handling user authentication
// const authService = {
//   // Register new user
//   register: async (userData) => {
//     try {
//       const response = await apiClient.post(USER_ENDPOINTS.REGISTER, userData);
//       return response.data;
//     } catch (error) {
//       throw error.response?.data || error;
//     }
//   },

//   // Mock login - In production, use actual auth endpoint
//  // Real Login - Calling your Spring Boot Backend
//  login: async (email, password) => {
//     try {
//       // Perform the POST request to /v1/auth/login
//       const response = await apiClient.post('/v1/auth/login', { email, password });
      
//       // Extract data from Spring Boot's standard wrapper if present
//       const result = response.data.data || response.data; 
      
//       if (result.token) {
//         // Standardized key: 'token'
//         localStorage.setItem('token', result.token);
//         localStorage.setItem('user', JSON.stringify(result.user));
//         return result; 
//       }
      
//       throw new Error('Invalid response from server: Token missing');
//     } catch (error) {
//       // Re-throw the specific error from the backend
//       throw error.response?.data || error;
//     }
//   },
//   // Logout
//   logout: () => {
//     localStorage.removeItem('authToken');
//     localStorage.removeItem('user');
//   },

//   // Change password
//   changePassword: async (userId, oldPassword, newPassword) => {
//     try {
//       const response = await apiClient.post(
//         USER_ENDPOINTS.CHANGE_PASSWORD(userId),
//         { oldPassword, newPassword }
//       );
//       return response.data;
//     } catch (error) {
//       throw error.response?.data || error;
//     }
//   },

//   // Reset password
//   resetPassword: async (email) => {
//     try {
//       const response = await apiClient.post(USER_ENDPOINTS.RESET_PASSWORD, { email });
//       return response.data;
//     } catch (error) {
//       throw error.response?.data || error;
//     }
//   },

//   // Get current user
//   getCurrentUser: () => {
//     const user = localStorage.getItem('user');
//     return user ? JSON.parse(user) : null;
//   },

//   // Check if user is authenticated
//   isAuthenticated: () => {
//     return !!localStorage.getItem('authToken');
//   },

//   // Get auth token
//   getToken: () => {
//     return localStorage.getItem('authToken');
//   },
// };

// export default authService;
