import axios from 'axios';

// API Base Configuration
const API_BASE_URL = '';

// Create axios instance
// const apiClient = axios.create({
//   baseURL: API_BASE_URL,
//   headers: {
//     'Content-Type': 'application/json',
//   },
// });
const apiClient = axios.create({
  baseURL: API_BASE_URL, // <--- MAKE SURE THIS LINE IS HERE
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json',
  },
});
// Request interceptor - Add JWT token to requests
apiClient.interceptors.request.use(
  (config) => {
    // Standardized to 'token'
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor - Handle token expiration
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Must match the key used in authService
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      
      // Only redirect if we aren't already on the login page
      if (!window.location.pathname.includes('/login')) {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default apiClient;
