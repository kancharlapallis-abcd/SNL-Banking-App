import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import apiClient from '../services/apiClient'; 
import transactionService from '../services/transactionService';
import { TRANSACTION_ENDPOINTS } from '../constants/apiEndpoints';

// 1. Transfer Funds Thunk
// src/store/transactionSlice.js

// src/store/transactionSlice.js
// src/store/transactionSlice.js
// src/store/transactionSlice.js

// FIX FOR TRANSFER: Get the inner .data from the APIResponseDTO
export const transferFunds = createAsyncThunk(
  'transaction/transferFunds',
  async (transferData, { rejectWithValue }) => {
    try {
      const result = await transactionService.transferFunds(transferData);
      // result is the APIResponseDTO
      if (result.statusCode === 200 || result.success) {
        return result.data; // The actual Transaction object
      }
      return rejectWithValue(result.message);
    } catch (err) {
      return rejectWithValue(err.response?.data?.message || "Server Error");
    }
  }
);

// FIX FOR DASHBOARD: Go into .data then into .content
export const getAccountTransactions = createAsyncThunk(
  'transaction/getAccountTransactions',
  async ({ accountId, page, size }, { rejectWithValue }) => {
    try {
      const result = await transactionService.getAccountTransactions(accountId, page, size);
      // result.data is the Spring Page object
      // result.data.content is the Array of transactions
      return result.data.content || result.data || []; 
    } catch (err) {
      return rejectWithValue(err.response?.data?.message || "Failed to fetch");
    }
  }
);

// 3. Get Transaction History
export const getTransactionHistory = createAsyncThunk(
  'transaction/getTransactionHistory',
  async ({ accountId, page = 0, size = 10 }, { rejectWithValue }) => {
    try {
      const response = await transactionService.getTransactionHistory(accountId, page, size);
      // Unwrap the Pageable content
      return response.data.content;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

// 4. Get Transaction By ID
export const getTransactionById = createAsyncThunk(
  'transaction/getTransactionById',
  async (transactionId, { rejectWithValue }) => {
    try {
      const response = await transactionService.getTransactionById(transactionId);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

// 5. Reverse Transaction
export const reverseTransaction = createAsyncThunk(
  'transaction/reverseTransaction',
  async (transactionId, { rejectWithValue }) => {
    try {
      const response = await transactionService.reverseTransaction(transactionId);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

const initialState = {
  transactions: [],
  selectedTransaction: null,
  loading: false,
  error: null,
  successMessage: null,
};

const transactionSlice = createSlice({
  name: 'transaction',
  initialState,
  reducers: {
    clearError: (state) => { state.error = null; },
    clearSuccessMessage: (state) => { state.successMessage = null; },
  },
  extraReducers: (builder) => {
    builder
      // Transfer Funds
      .addCase(transferFunds.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(transferFunds.fulfilled, (state, action) => {
        state.loading = false;
        if (action.payload) {
          state.transactions = [action.payload, ...state.transactions];
        }
      })
      .addCase(transferFunds.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      // Get Account Transactions
      .addCase(getAccountTransactions.pending, (state) => {
        state.loading = true;
      })
      .addCase(getAccountTransactions.fulfilled, (state, action) => {
        state.loading = false;
        // action.payload is now the clean array of transactions
        state.transactions = Array.isArray(action.payload) ? action.payload : [];
      })
      .addCase(getAccountTransactions.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
      // (Add other cases for history, id, reverse as needed following the same pattern)
  },
});

export const { clearError, clearSuccessMessage } = transactionSlice.actions;
export default transactionSlice.reducer;