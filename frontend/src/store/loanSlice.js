import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import loanService from '../services/loanService';

/**
 * Loan Thunks
 */
export const submitLoanApplication = createAsyncThunk(
  'loan/submitLoanApplication',
  async (loanData, { rejectWithValue }) => {
    try {
      // Ensure the service method name matches exactly!
      const response = await loanService.submitLoanApplication(loanData);
      
      // result is your APIResponseDTO { statusCode, data, message, success }
      const result = response.data;

      if (result.success || result.statusCode === 200 || result.statusCode === 201) {
        return result.data; // Return the loan object
      } else {
        return rejectWithValue(result.message || "Backend rejected application");
      }
    } catch (error) {
      // This catches 404, 500, or the crash if the URL was undefined
      return rejectWithValue(error.response?.data?.message || error.message || "Connection Error");
    }
  }
);

export const getUserLoans = createAsyncThunk(
  'loan/getUserLoans',
  async ({ userId, page = 0, size = 10 }, { rejectWithValue }) => {
    try {
      const response = await loanService.getUserLoans(userId, page, size);
      return response.data;
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || 'Failed to fetch loans'
      );
    }
  }
);

export const getAllLoans = createAsyncThunk(
  'loan/getAllLoans',
  async ({ page = 0, size = 10 }, { rejectWithValue }) => {
    try {
      const response = await loanService.getAllLoans(page, size);
      return response.data;
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || 'Failed to fetch loans'
      );
    }
  }
);

export const approveLoan = createAsyncThunk(
  'loan/approveLoan',
  async (loanId, { rejectWithValue }) => {
    try {
      const response = await loanService.approveLoan(loanId);
      return response.data;
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || 'Failed to approve loan'
      );
    }
  }
);

export const rejectLoan = createAsyncThunk(
  'loan/rejectLoan',
  async (loanId, { rejectWithValue }) => {
    try {
      const response = await loanService.rejectLoan(loanId);
      return response.data;
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || 'Failed to reject loan'
      );
    }
  }
);

export const getLoanById = createAsyncThunk(
  'loan/getLoanById',
  async (loanId, { rejectWithValue }) => {
    try {
      const response = await loanService.getLoanById(loanId);
      return response.data;
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || 'Failed to fetch loan'
      );
    }
  }
);

/**
 * Loan Slice
 */
const initialState = {
  loans: [],
  selectedLoan: null,
  loading: false,
  error: null,
  successMessage: null,
  pagination: {
    page: 0,
    size: 10,
    totalPages: 0,
    totalElements: 0
  }
};

const loanSlice = createSlice({
  name: 'loan',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    clearSuccess: (state) => {
      state.successMessage = null;
    },
    selectLoan: (state, action) => {
      state.selectedLoan = action.payload;
    }
  },
  extraReducers: (builder) => {
    // Submit Loan Application
    builder
      .addCase(submitLoanApplication.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(submitLoanApplication.fulfilled, (state, action) => {
  state.loading = false;
  state.successMessage = 'Loan application submitted successfully';
  
  // Ensure we only add the DATA object, not the whole API response
  const newLoan = action.payload.data || action.payload; 
  
  if (newLoan && typeof newLoan === 'object') {
    state.loans = [newLoan, ...state.loans];
  }
})
      .addCase(submitLoanApplication.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Get User Loans
    builder
      .addCase(getUserLoans.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(getUserLoans.fulfilled, (state, action) => {
        state.loading = false;
        // Handle pagination: data.content for the array, data for pagination info
        state.loans = action.payload.data.content || action.payload.data;
        state.pagination = {
          page: action.payload.data.number || 0,
          size: action.payload.data.size || 10,
          totalPages: action.payload.data.totalPages || 0,
          totalElements: action.payload.data.totalElements || 0
        };
      })
      .addCase(getUserLoans.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Get All Loans
    builder
      .addCase(getAllLoans.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(getAllLoans.fulfilled, (state, action) => {
        state.loading = false;
        // Handle pagination: data.content for the array, data for pagination info
        state.loans = action.payload.data.content || action.payload.data;
        state.pagination = {
          page: action.payload.data.number || 0,
          size: action.payload.data.size || 10,
          totalPages: action.payload.data.totalPages || 0,
          totalElements: action.payload.data.totalElements || 0
        };
      })
      .addCase(getAllLoans.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Approve Loan
    builder
      .addCase(approveLoan.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(approveLoan.fulfilled, (state, action) => {
        state.loading = false;
        state.successMessage = 'Loan approved successfully';
        const loanIndex = state.loans.findIndex(loan => loan.loanApplicationId === action.meta.arg);
        if (loanIndex !== -1) {
          state.loans[loanIndex].status = 'ACTIVE';
        }
      })
      .addCase(approveLoan.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Reject Loan
    builder
      .addCase(rejectLoan.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(rejectLoan.fulfilled, (state, action) => {
        state.loading = false;
        state.successMessage = 'Loan rejected successfully';
        const loanIndex = state.loans.findIndex(loan => loan.loanApplicationId === action.meta.arg);
        if (loanIndex !== -1) {
          state.loans[loanIndex].status = 'REJECTED';
        }
      })
      .addCase(rejectLoan.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Get Loan By ID
    builder
      .addCase(getLoanById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(getLoanById.fulfilled, (state, action) => {
        state.loading = false;
        state.selectedLoan = action.payload.data;
      })
      .addCase(getLoanById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
  }
});

export const { clearError, clearSuccess, selectLoan } = loanSlice.actions;
export default loanSlice.reducer;
