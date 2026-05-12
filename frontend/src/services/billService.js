import apiClient from './apiClient';
import { API_ENDPOINTS } from '../constants/apiEndpoints';

const billService = {
  /**
   * Pay a bill
   */
  payBill: (billPaymentData) => {
    return apiClient.post(API_ENDPOINTS.BILL_ENDPOINTS.PAY_BILL, billPaymentData);
  },

  /**
   * Get bill payment by ID
   */
  getBillPaymentById: (billPaymentId) => {
    return apiClient.get(API_ENDPOINTS.BILL_ENDPOINTS.GET_BILL_PAYMENT(billPaymentId));
  },

  /**
   * Get all bill payments for an account
   */
  getAccountBillPayments: (accountId, page = 0, size = 10) => {
    return apiClient.get(
      API_ENDPOINTS.BILL_ENDPOINTS.GET_ACCOUNT_BILL_PAYMENTS(accountId),
      {
        params: { page, size }
      }
    );
  },

  /**
   * Get all bill payments (paginated)
   */
  getAllBillPayments: (page = 0, size = 10) => {
    return apiClient.get(API_ENDPOINTS.BILL_ENDPOINTS.GET_ALL_BILL_PAYMENTS, {
      params: { page, size }
    });
  },

  /**
   * Cancel a bill payment
   */
  cancelBillPayment: (billPaymentId) => {
    return apiClient.post(
      API_ENDPOINTS.BILL_ENDPOINTS.CANCEL_BILL_PAYMENT(billPaymentId)
    );
  },

  /**
   * Get bill payment by reference
   */
  getBillPaymentByReference: (billReference) => {
    return apiClient.get(
      API_ENDPOINTS.BILL_ENDPOINTS.GET_BILL_PAYMENT_BY_REFERENCE(billReference)
    );
  }
};

export default billService;
