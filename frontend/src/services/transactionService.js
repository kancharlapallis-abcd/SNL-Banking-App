import apiClient from './apiClient';
import { TRANSACTION_ENDPOINTS } from '../constants/apiEndpoints';

// src/services/transactionService.js
const transactionService = {
  transferFunds: async (transferData) => {
    // RETURNS: { statusCode, message, data: { transactionId, ... } }
    const response = await apiClient.post(TRANSACTION_ENDPOINTS.TRANSFER_FUNDS, transferData);
    return response.data; 
  },
  
  getAccountTransactions: async (accountId, page = 0, size = 10) => {
    // RETURNS: { statusCode, message, data: { content: [...], totalElements: 10 } }
    const response = await apiClient.get(TRANSACTION_ENDPOINTS.GET_ACCOUNT_TRANSACTIONS(accountId), {
      params: { page, size }
    });
    return response.data;
  },
  // ... other methods


  // Get transaction by ID
  getTransactionById: async (transactionId) => {
    try {
      const response = await apiClient.get(TRANSACTION_ENDPOINTS.GET_TRANSACTION(transactionId));
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get transaction by reference
  getTransactionByReference: async (reference) => {
    try {
      const response = await apiClient.get(
        TRANSACTION_ENDPOINTS.GET_TRANSACTION_BY_REFERENCE(reference)
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get all transactions (Admin)
  getAllTransactions: async (page = 0, size = 10) => {
    try {
      const response = await apiClient.get(TRANSACTION_ENDPOINTS.GET_ALL_TRANSACTIONS, {
        params: { page, size },
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get account transactions
  getAccountTransactions: async (accountId, page = 0, size = 10) => {
    try {
      const response = await apiClient.get(
        TRANSACTION_ENDPOINTS.GET_ACCOUNT_TRANSACTIONS(accountId),
        { params: { page, size } }
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get outgoing transactions
  getOutgoingTransactions: async (accountId, page = 0, size = 10) => {
    try {
      const response = await apiClient.get(
        TRANSACTION_ENDPOINTS.GET_OUTGOING_TRANSACTIONS(accountId),
        { params: { page, size } }
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get incoming transactions
  getIncomingTransactions: async (accountId, page = 0, size = 10) => {
    try {
      const response = await apiClient.get(
        TRANSACTION_ENDPOINTS.GET_INCOMING_TRANSACTIONS(accountId),
        { params: { page, size } }
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get transaction history
  getTransactionHistory: async (accountId, page = 0, size = 10) => {
    try {
      const response = await apiClient.get(
        TRANSACTION_ENDPOINTS.GET_TRANSACTION_HISTORY(accountId),
        { params: { page, size } }
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Reverse transaction
  reverseTransaction: async (transactionId) => {
    try {
      const response = await apiClient.post(
        TRANSACTION_ENDPOINTS.REVERSE_TRANSACTION(transactionId)
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Get total outgoing amount
  getTotalOutgoing: async (accountId) => {
    try {
      const response = await apiClient.get(
        TRANSACTION_ENDPOINTS.GET_TOTAL_OUTGOING(accountId)
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },
};

export default transactionService;
