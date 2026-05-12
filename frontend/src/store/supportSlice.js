import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import supportService from '../services/supportService';

/**
 * Support Ticket Thunks
 */
export const submitSupportTicket = createAsyncThunk(
  'support/submitSupportTicket',
  async (ticketData, { rejectWithValue }) => {
    try {
      const response = await supportService.submitSupportTicket(ticketData);
      return response.data;
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || 'Failed to submit support ticket'
      );
    }
  }
);

export const getUserSupportTickets = createAsyncThunk(
  'support/getUserSupportTickets',
  async ({ userId, page = 0, size = 10 }, { rejectWithValue }) => {
    try {
      const response = await supportService.getUserSupportTickets(userId, page, size);
      return response.data;
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || 'Failed to fetch support tickets'
      );
    }
  }
);

export const getAllSupportTickets = createAsyncThunk(
  'support/getAllSupportTickets',
  async ({ page = 0, size = 10 }, { rejectWithValue }) => {
    try {
      const response = await supportService.getAllSupportTickets(page, size);
      return response.data;
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || 'Failed to fetch support tickets'
      );
    }
  }
);

export const getSupportTicketById = createAsyncThunk(
  'support/getSupportTicketById',
  async (ticketId, { rejectWithValue }) => {
    try {
      const response = await supportService.getSupportTicketById(ticketId);
      return response.data;
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || 'Failed to fetch support ticket'
      );
    }
  }
);

export const closeTicket = createAsyncThunk(
  'support/closeTicket',
  async (ticketId, { rejectWithValue }) => {
    try {
      const response = await supportService.closeTicket(ticketId);
      return response.data;
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || 'Failed to close support ticket'
      );
    }
  }
);

/**
 * Support Slice
 */
const initialState = {
  tickets: [],
  selectedTicket: null,
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

const supportSlice = createSlice({
  name: 'support',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    clearSuccess: (state) => {
      state.successMessage = null;
    },
    selectTicket: (state, action) => {
      state.selectedTicket = action.payload;
    }
  },
  extraReducers: (builder) => {
    // Submit Support Ticket
    builder
      .addCase(submitSupportTicket.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(submitSupportTicket.fulfilled, (state, action) => {
        state.loading = false;
        state.successMessage = 'Support ticket submitted successfully';
        state.selectedTicket = action.payload.data;
        state.tickets.push(action.payload.data);
      })
      .addCase(submitSupportTicket.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Get User Support Tickets
    builder
      .addCase(getUserSupportTickets.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(getUserSupportTickets.fulfilled, (state, action) => {
        state.loading = false;
        // Handle pagination: data.content for the array, data for pagination info
        state.tickets = action.payload.data.content || action.payload.data;
        state.pagination = {
          page: action.payload.data.number || 0,
          size: action.payload.data.size || 10,
          totalPages: action.payload.data.totalPages || 0,
          totalElements: action.payload.data.totalElements || 0
        };
      })
      .addCase(getUserSupportTickets.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Get All Support Tickets
    builder
      .addCase(getAllSupportTickets.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(getAllSupportTickets.fulfilled, (state, action) => {
        state.loading = false;
        // Handle pagination: data.content for the array, data for pagination info
        state.tickets = action.payload.data.content || action.payload.data;
        state.pagination = {
          page: action.payload.data.number || 0,
          size: action.payload.data.size || 10,
          totalPages: action.payload.data.totalPages || 0,
          totalElements: action.payload.data.totalElements || 0
        };
      })
      .addCase(getAllSupportTickets.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Get Support Ticket By ID
    builder
      .addCase(getSupportTicketById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(getSupportTicketById.fulfilled, (state, action) => {
        state.loading = false;
        state.selectedTicket = action.payload.data;
      })
      .addCase(getSupportTicketById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });

    // Close Ticket
    builder
      .addCase(closeTicket.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(closeTicket.fulfilled, (state, action) => {
        state.loading = false;
        state.successMessage = 'Support ticket closed successfully';
        const ticketIndex = state.tickets.findIndex(ticket => ticket.ticketId === action.meta.arg);
        if (ticketIndex !== -1) {
          state.tickets[ticketIndex].status = 'CLOSED';
        }
      })
      .addCase(closeTicket.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
  }
});

export const { clearError, clearSuccess, selectTicket } = supportSlice.actions;
export default supportSlice.reducer;
