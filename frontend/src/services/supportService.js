import apiClient from './apiClient';
import { API_ENDPOINTS } from '../constants/apiEndpoints';

const supportService = {
  /**
   * Submit a support ticket
   */
  submitSupportTicket: (ticketData) => {
    return apiClient.post(API_ENDPOINTS.SUPPORT_ENDPOINTS.SUBMIT_TICKET, ticketData);
  },

  /**
   * Get support ticket by ID
   */
  getSupportTicketById: (ticketId) => {
    return apiClient.get(API_ENDPOINTS.SUPPORT_ENDPOINTS.GET_TICKET(ticketId));
  },

  /**
   * Get all support tickets for a user
   */
  getUserSupportTickets: (userId, page = 0, size = 10) => {
    return apiClient.get(
      API_ENDPOINTS.SUPPORT_ENDPOINTS.GET_USER_TICKETS(userId),
      {
        params: { page, size }
      }
    );
  },

  /**
   * Get all support tickets (paginated)
   */
  getAllSupportTickets: (page = 0, size = 10) => {
    return apiClient.get(API_ENDPOINTS.SUPPORT_ENDPOINTS.GET_ALL_TICKETS, {
      params: { page, size }
    });
  },

  /**
   * Close a support ticket
   */
  closeTicket: (ticketId) => {
    return apiClient.post(
      API_ENDPOINTS.SUPPORT_ENDPOINTS.CLOSE_TICKET(ticketId)
    );
  },

  /**
   * Add a reply to a ticket
   */
  addReply: (ticketId, replyData) => {
    return apiClient.post(
      API_ENDPOINTS.SUPPORT_ENDPOINTS.ADD_REPLY(ticketId),
      replyData
    );
  }
};

export default supportService;
