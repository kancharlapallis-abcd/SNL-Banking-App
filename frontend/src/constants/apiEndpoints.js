/**
 * API Endpoints Configuration
 * FIXED: Added /api context-path to all base routes.
 * Organized by module for Phase 1 and Phase 2 services.
 */

// 1. Auth Endpoints
export const AUTH_ENDPOINTS = {
  LOGIN: '/api/v1/users/login',
  REGISTER: '/api/v1/users/register',
  REFRESH_TOKEN: '/api/v1/users/refresh',
};

// 2. User Management Endpoints
export const USER_ENDPOINTS = {
  REGISTER: '/api/v1/users/register',
  GET_USER: (id) => `/api/v1/users/${id}`,
  GET_ALL_USERS: '/api/v1/users',
  UPDATE_USER: (id) => `/api/v1/users/${id}`,
  CHANGE_PASSWORD: (id) => `/api/v1/users/${id}/change-password`,
  RESET_PASSWORD: '/api/v1/users/reset-password',
  ACTIVATE_USER: (id) => `/api/v1/users/${id}/activate`,
  DEACTIVATE_USER: (id) => `/api/v1/users/${id}/deactivate`,
  DELETE_USER: (id) => `/api/v1/users/${id}`,
  SEARCH_USERS: '/api/v1/users/search',
};

// 3. Account Management Endpoints
export const ACCOUNT_ENDPOINTS = {
  CREATE_ACCOUNT: '/api/v1/accounts',
  GET_ACCOUNT: (id) => `/api/v1/accounts/${id}`,
  GET_ACCOUNT_BY_NUMBER: (accountNumber) => `/api/v1/accounts/number/${accountNumber}`,
  GET_ALL_ACCOUNTS: '/api/v1/accounts',
  GET_USER_ACCOUNTS: (userId) => `/api/v1/accounts/user/${userId}`,
  GET_ACCOUNT_BALANCE: (id) => `/api/v1/accounts/${id}/balance`,
  UPDATE_ACCOUNT: (id) => `/api/v1/accounts/${id}`,
  CLOSE_ACCOUNT: (id) => `/api/v1/accounts/${id}/close`,
  FREEZE_ACCOUNT: (id) => `/api/v1/accounts/${id}/freeze`,
  UNFREEZE_ACCOUNT: (id) => `/api/v1/accounts/${id}/unfreeze`,
};

// 4. Transaction Endpoints
export const TRANSACTION_ENDPOINTS = {
  TRANSFER_FUNDS: '/api/v1/transactions/transfer',
  GET_TRANSACTION: (id) => `/api/v1/transactions/${id}`,
  GET_TRANSACTION_BY_REFERENCE: (reference) => `/api/v1/transactions/reference/${reference}`,
  GET_ALL_TRANSACTIONS: '/api/v1/transactions',
  GET_ACCOUNT_TRANSACTIONS: (accountId) => `/api/v1/transactions/account/${accountId}`,
  GET_OUTGOING_TRANSACTIONS: (accountId) => `/api/v1/transactions/account/${accountId}/outgoing`,
  GET_INCOMING_TRANSACTIONS: (accountId) => `/api/v1/transactions/account/${accountId}/incoming`,
  GET_TRANSACTION_HISTORY: (accountId) => `/api/v1/transactions/account/${accountId}/history`,
  REVERSE_TRANSACTION: (id) => `/api/v1/transactions/${id}/reverse`,
  GET_TOTAL_OUTGOING: (accountId) => `/api/v1/transactions/account/${accountId}/total-outgoing`,
};

// 5. Loan Endpoints (Phase 2)
export const LOAN_ENDPOINTS = {
  APPLY_LOAN: '/api/v1/loans/apply',
  GET_LOAN: (id) => `/api/v1/loans/${id}`,
  GET_USER_LOANS: (userId) => `/api/v1/loans/user/${userId}`,
  GET_ALL_LOANS: '/api/v1/loans',
  APPROVE_LOAN: (id) => `/api/v1/loans/${id}/approve`,
  REJECT_LOAN: (id) => `/api/v1/loans/${id}/reject`,
  GET_LOAN_BY_NUMBER: (loanNumber) => `/api/v1/loans/number/${loanNumber}`,
};

// 6. Bill Payment Endpoints (Phase 2)
export const BILL_ENDPOINTS = {
  PAY_BILL: '/api/v1/bills/pay',
  GET_BILL_PAYMENT: (id) => `/api/v1/bills/${id}`,
  GET_ACCOUNT_BILL_PAYMENTS: (accountId) => `/api/v1/bills/account/${accountId}`,
  GET_ALL_BILL_PAYMENTS: '/api/v1/bills',
  CANCEL_BILL_PAYMENT: (id) => `/api/v1/bills/${id}/cancel`,
  GET_BILL_PAYMENT_BY_REFERENCE: (reference) => `/api/v1/bills/reference/${reference}`,
};

// 7. Support Ticket Endpoints (Phase 2)
export const SUPPORT_ENDPOINTS = {
  SUBMIT_TICKET: '/api/v1/support/tickets',
  GET_TICKET: (id) => `/api/v1/support/tickets/${id}`,
  GET_USER_TICKETS: (userId) => `/api/v1/support/tickets/user/${userId}`,
  GET_ALL_TICKETS: '/api/v1/support/tickets',
  CLOSE_TICKET: (id) => `/api/v1/support/tickets/${id}/close`,
  ADD_REPLY: (ticketId) => `/api/v1/support/tickets/${ticketId}/reply`,
};

/**
 * MASTER API OBJECT
 */
/**
 * MASTER API OBJECT
 * FIXED: Renamed keys to match service calls (e.g., LOAN_ENDPOINTS)
 */
export const API_ENDPOINTS = {
  BILL_ENDPOINTS: BILL_ENDPOINTS,        // If service uses API_ENDPOINTS.BILLS
  LOAN_ENDPOINTS: LOAN_ENDPOINTS, // CHANGED from LOANS
  SUPPORT: SUPPORT_ENDPOINTS,
  ACCOUNTS: ACCOUNT_ENDPOINTS,
  AUTH: AUTH_ENDPOINTS,
  USER: USER_ENDPOINTS,
  TRANSACTIONS: TRANSACTION_ENDPOINTS,
};