import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import billService from '../services/billService';

/**
 * Bill Payment Thunks
 */
export const payBill = createAsyncThunk(
  'bill/payBill',
  async (billData, { rejectWithValue }) => {
    try {
      const response = await billService.payBill(billData);
      return response.data;
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || 'Failed to process bill payment'
      );
    }
  }
);

export const getAccountBillPayments = createAsyncThunk(
  'bill/getAccountBillPayments',
  async ({ accountId, page = 0, size = 10 }, { rejectWithValue }) => {
    try {
      const response = await billService.getAccountBillPayments(accountId, page, size);
      return response.data;
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || 'Failed to fetch bill payments'
      );
    }
  }
);

export const getAllBillPayments = createAsyncThunk(
  'bill/getAllBillPayments',
  async ({ page = 0, size = 10 }, { rejectWithValue }) => {
    try {
      const response = await billService.getAllBillPayments(page, size);
      return response.data;
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || 'Failed to fetch bill payments'
      );
    }
  }
);

export const cancelBillPayment = createAsyncThunk(
  'bill/cancelBillPayment',
  async (billPaymentId, { rejectWithValue }) => {
    try {
      const response = await billService.cancelBillPayment(billPaymentId);
      return response.data;
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || 'Failed to cancel bill payment'
      );
    }
  }
);

export const getBillPaymentById = createAsyncThunk(
  'bill/getBillPaymentById',
  async (billPaymentId, { rejectWithValue }) => {
    try {
      const response = await billService.getBillPaymentById(billPaymentId);
      return response.data;
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || 'Failed to fetch bill payment'
      );
    }
  }
);

/**
 * Bill Slice
 */
const initialState = {
  bills: [],
  selectedBill: null,
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

const billSlice = createSlice({
  name: 'bill',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    clearSuccess: (state) => {
      state.successMessage = null;
    },
    selectBill: (state, action) => {
      state.selectedBill = action.payload;
    }
  },
  extraReducers: (builder) => {
    // Pay Bill
    builder
      .addCase(payBill.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(payBill.fulfilled, (state, action) => {
        state.loading = false;
        state.successMessage = 'Bill payment processed successfully';
        state.selectedBill = action.payload.data;
      })
      .addCase(payBill.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Get Account Bill Payments
    builder
      .addCase(getAccountBillPayments.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
     // src/store/billSlice.js

.addCase(getAccountBillPayments.fulfilled, (state, action) => {
  state.loading = false;
  // Deep check: extract the array from Spring Boot's Page object or the raw data
  const fetchedBills = action.payload?.data?.content || action.payload?.data || action.payload;
  
  state.bills = Array.isArray(fetchedBills) ? fetchedBills : [];
  
  state.pagination = {
    page: action.payload?.data?.number || 0,
    size: action.payload?.data?.size || 10,
    totalElements: action.payload?.data?.totalElements || 0,
    totalPages: action.payload?.data?.totalPages || 0
  };
})
      .addCase(getAccountBillPayments.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Get All Bill Payments
    builder
      .addCase(getAllBillPayments.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(getAllBillPayments.fulfilled, (state, action) => {
        state.loading = false;
        // Handle pagination: data.content for the array, data for pagination info
        state.bills = action.payload.data.content || action.payload.data;
        state.pagination = {
          page: action.payload.data.number || 0,
          size: action.payload.data.size || 10,
          totalPages: action.payload.data.totalPages || 0,
          totalElements: action.payload.data.totalElements || 0
        };
      })
      .addCase(getAllBillPayments.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Cancel Bill Payment
    builder
      .addCase(cancelBillPayment.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(cancelBillPayment.fulfilled, (state, action) => {
        state.loading = false;
        state.successMessage = 'Bill payment cancelled successfully';
        state.bills = state.bills.filter(bill => bill.billPaymentId !== action.meta.arg);
      })
      .addCase(cancelBillPayment.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Get Bill Payment By ID
    builder
      .addCase(getBillPaymentById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(getBillPaymentById.fulfilled, (state, action) => {
        state.loading = false;
        state.selectedBill = action.payload.data;
      })
      .addCase(getBillPaymentById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
  }
});

export const { clearError, clearSuccess, selectBill } = billSlice.actions;
export default billSlice.reducer;
