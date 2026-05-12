package com.snlbanking.service;

import com.snlbanking.dto.SupportTicketRequestDTO;
import com.snlbanking.dto.SupportTicketResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Support Ticket Service Interface
 * Defines contract for support ticket operations
 */
public interface SupportTicketService {
  /**
   * Submit a new support ticket
   */
  SupportTicketResponseDTO submitTicket(SupportTicketRequestDTO request);

  /**
   * Get support ticket by ID
   */
  SupportTicketResponseDTO getTicketById(Long ticketId);

  /**
   * Get all support tickets for a user
   */
  Page<SupportTicketResponseDTO> getUserTickets(Long userId, Pageable pageable);

  /**
   * Get all support tickets (paginated)
   */
  Page<SupportTicketResponseDTO> getAllTickets(Pageable pageable);

  /**
   * Close a support ticket
   */
  SupportTicketResponseDTO closeTicket(Long ticketId);

  /**
   * Get ticket by ticket number
   */
  SupportTicketResponseDTO getTicketByNumber(String ticketNumber);
}
