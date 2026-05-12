import apiClient from './apiClient';
import { USER_ENDPOINTS } from '../constants/apiEndpoints';

const userService = {
  // Get user by ID
  getUserById: async (userId) => {
    try {
      const response = await apiClient.get(USER_ENDPOINTS.GET_USER(userId));
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get all users (Admin)
  getAllUsers: async (page = 0, size = 10) => {
    try {
      const response = await apiClient.get(USER_ENDPOINTS.GET_ALL_USERS, {
        params: { page, size },
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Update user profile
  updateUserProfile: async (userId, userData) => {
    try {
      const response = await apiClient.put(USER_ENDPOINTS.UPDATE_USER(userId), userData);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Activate user
  activateUser: async (userId) => {
    try {
      const response = await apiClient.post(USER_ENDPOINTS.ACTIVATE_USER(userId));
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Deactivate user
  deactivateUser: async (userId) => {
    try {
      const response = await apiClient.post(USER_ENDPOINTS.DEACTIVATE_USER(userId));
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Delete user
  deleteUser: async (userId) => {
    try {
      const response = await apiClient.delete(USER_ENDPOINTS.DELETE_USER(userId));
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Search users by name
  searchUsers: async (name) => {
    try {
      const response = await apiClient.get(USER_ENDPOINTS.SEARCH_USERS, {
        params: { name },
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },
};

export default userService;
