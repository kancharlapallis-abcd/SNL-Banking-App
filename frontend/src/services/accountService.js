import apiClient from './apiClient';
import { ACCOUNT_ENDPOINTS } from '../constants/apiEndpoints';

const accountService = {
  // Create new account
  createAccount: async (accountData) => {
    try {
      const response = await apiClient.post(ACCOUNT_ENDPOINTS.CREATE_ACCOUNT, accountData);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get account by ID
  getAccountById: async (accountId) => {
    try {
      const response = await apiClient.get(ACCOUNT_ENDPOINTS.GET_ACCOUNT(accountId));
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get account by account number
  getAccountByNumber: async (accountNumber) => {
    try {
      const response = await apiClient.get(
        ACCOUNT_ENDPOINTS.GET_ACCOUNT_BY_NUMBER(accountNumber)
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get all accounts (Admin)
  getAllAccounts: async (page = 0, size = 10) => {
    try {
      const response = await apiClient.get(ACCOUNT_ENDPOINTS.GET_ALL_ACCOUNTS, {
        params: { page, size },
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get user accounts
  getUserAccounts: async (userId) => {
    try {
      const response = await apiClient.get(ACCOUNT_ENDPOINTS.GET_USER_ACCOUNTS(userId));
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get account balance
  getAccountBalance: async (accountId) => {
    try {
      const response = await apiClient.get(ACCOUNT_ENDPOINTS.GET_ACCOUNT_BALANCE(accountId));
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Update account details
  updateAccount: async (accountId, accountData) => {
    try {
      const response = await apiClient.put(ACCOUNT_ENDPOINTS.UPDATE_ACCOUNT(accountId), accountData);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Close account
  closeAccount: async (accountId) => {
    try {
      const response = await apiClient.post(ACCOUNT_ENDPOINTS.CLOSE_ACCOUNT(accountId));
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Freeze account
  freezeAccount: async (accountId) => {
    try {
      const response = await apiClient.post(ACCOUNT_ENDPOINTS.FREEZE_ACCOUNT(accountId));
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Unfreeze account
  unfreezeAccount: async (accountId) => {
    try {
      const response = await apiClient.post(ACCOUNT_ENDPOINTS.UNFREEZE_ACCOUNT(accountId));
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },
};

export default accountService;
