import apiClient from './apiClient';
import { API_ENDPOINTS } from '../constants/apiEndpoints';

const loanService = {
  submitLoanApplication: async (loanApplicationData) => {
    // Safety check: if this prints undefined, your constants file is the problem
    console.log("Target URL:", API_ENDPOINTS?.LOAN_ENDPOINTS?.APPLY_LOAN);
    
    const response = await apiClient.post(
      API_ENDPOINTS.LOAN_ENDPOINTS.APPLY_LOAN, 
      loanApplicationData
    );
    return response;
  },

  /**
   * Get loan by ID
   */
  getLoanById: (loanId) => {
    return apiClient.get(API_ENDPOINTS.LOAN_ENDPOINTS.GET_LOAN(loanId));
  },

  getUserLoans: async (userId, page = 0, size = 10) => {
    const response = await apiClient.get(
      API_ENDPOINTS.LOAN_ENDPOINTS.GET_USER_LOANS(userId),
      { params: { page, size } }
    );
    return response;
  },

  /**
   * Get all loans (paginated)
   */
  getAllLoans: (page = 0, size = 10) => {
    return apiClient.get(API_ENDPOINTS.LOAN_ENDPOINTS.GET_ALL_LOANS, {
      params: { page, size }
    });
  },

  /**
   * Approve a loan application
   */
  approveLoan: (loanId) => {
    return apiClient.post(
      API_ENDPOINTS.LOAN_ENDPOINTS.APPROVE_LOAN(loanId)
    );
  },

  /**
   * Reject a loan application
   */
  rejectLoan: (loanId) => {
    return apiClient.post(
      API_ENDPOINTS.LOAN_ENDPOINTS.REJECT_LOAN(loanId)
    );
  },

  /**
   * Get loan by number
   */
  getLoanByNumber: (loanNumber) => {
    return apiClient.get(
      API_ENDPOINTS.LOAN_ENDPOINTS.GET_LOAN_BY_NUMBER(loanNumber)
    );
  }
};

export default loanService;
