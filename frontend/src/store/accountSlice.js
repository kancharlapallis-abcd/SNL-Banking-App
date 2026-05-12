import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
// import accountService from '../../services/accountService';
import accountService from '../services/accountService'; // This stays inside 'src'

// Thunks
export const createAccount = createAsyncThunk(
  'account/createAccount',
  async (accountData, { rejectWithValue }) => {
    try {
      const response = await accountService.createAccount(accountData);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

export const getUserAccounts = createAsyncThunk(
  'account/getUserAccounts',
  async (userId, { rejectWithValue }) => {
    try {
      const response = await accountService.getUserAccounts(userId);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

export const getAccountBalance = createAsyncThunk(
  'account/getAccountBalance',
  async (accountId, { rejectWithValue }) => {
    try {
      const response = await accountService.getAccountBalance(accountId);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

export const getAccountById = createAsyncThunk(
  'account/getAccountById',
  async (accountId, { rejectWithValue }) => {
    try {
      const response = await accountService.getAccountById(accountId);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

export const freezeAccount = createAsyncThunk(
  'account/freezeAccount',
  async (accountId, { rejectWithValue }) => {
    try {
      const response = await accountService.freezeAccount(accountId);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

export const unfreezeAccount = createAsyncThunk(
  'account/unfreezeAccount',
  async (accountId, { rejectWithValue }) => {
    try {
      const response = await accountService.unfreezeAccount(accountId);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

const initialState = {
  accounts: [],
  selectedAccount: null,
  loading: false,
  error: null,
  successMessage: null,
};

const accountSlice = createSlice({
  name: 'account',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    clearSuccessMessage: (state) => {
      state.successMessage = null;
    },
    selectAccount: (state, action) => {
      state.selectedAccount = action.payload;
    },
  },
  extraReducers: (builder) => {
    // Create Account
    builder
      .addCase(createAccount.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
     .addCase(createAccount.fulfilled, (state, action) => {
  state.loading = false;
  // Ensure we only push the account object, not the whole response
  const newAccount = action.payload?.data || action.payload;
  state.accounts.push(newAccount);
  state.successMessage = 'Account created successfully';
})
      .addCase(createAccount.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Get User Accounts
    builder
      .addCase(getUserAccounts.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
    .addCase(getUserAccounts.fulfilled, (state, action) => {
  state.loading = false;
  // LOG THIS to confirm we see 'content'
  console.log("Full API Response:", action.payload);
  /**
   * FIX: Spring Data JPA Pagination puts the list inside 'content'.
   * We check action.payload.content first.
   */
  const fetchedData = action.payload?.content || action.payload?.data || action.payload;
  state.accounts = Array.isArray(fetchedData) ? fetchedData : [];
  state.error = null;
})
      .addCase(getUserAccounts.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Get Account Balance
    builder
      .addCase(getAccountBalance.pending, (state) => {
        state.loading = true;
      })
    .addCase(getAccountBalance.fulfilled, (state, action) => {
  state.loading = false;
  // Unwrap the data (handling both raw value or object)
  const balanceData = action.payload?.data || action.payload;
  const newBalance = typeof balanceData === 'object' ? balanceData.balance : balanceData;

  // 1. Update selectedAccount if it exists
  if (state.selectedAccount) {
    state.selectedAccount.balance = newBalance;
  }
  
  // 2. IMPORTANT: Find by accountId from the API response, not just from selectedAccount
  // We assume your balance API returns the accountId or you pass it in the thunk
  const accountIdFromAction = action.meta.arg; // The ID you passed to the dispatch
  
  const index = state.accounts.findIndex(a => a.accountId === accountIdFromAction);
  if (index !== -1) {
    state.accounts[index].balance = newBalance;
  }
})
      .addCase(getAccountBalance.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Get Account By ID
    builder
      .addCase(getAccountById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
     .addCase(getAccountById.fulfilled, (state, action) => {
  state.loading = false;
  // Unwrap here as well
  state.selectedAccount = action.payload?.data || action.payload;
})
      .addCase(getAccountById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Freeze Account
    builder
      .addCase(freezeAccount.pending, (state) => {
        state.loading = true;
      })
      .addCase(freezeAccount.fulfilled, (state, action) => {
        state.loading = false;
        state.successMessage = 'Account frozen successfully';
        if (state.selectedAccount) {
          state.selectedAccount.status = 'FROZEN';
        }
      })
      .addCase(freezeAccount.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Unfreeze Account
    builder
      .addCase(unfreezeAccount.pending, (state) => {
        state.loading = true;
      })
      .addCase(unfreezeAccount.fulfilled, (state, action) => {
        state.loading = false;
        state.successMessage = 'Account unfrozen successfully';
        if (state.selectedAccount) {
          state.selectedAccount.status = 'ACTIVE';
        }
      })
      .addCase(unfreezeAccount.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
  },
});

export const { clearError, clearSuccessMessage, selectAccount } = accountSlice.actions;
export default accountSlice.reducer;
