package com.snlbanking.service.impl;

import com.snlbanking.dto.SupportTicketRequestDTO;
import com.snlbanking.dto.SupportTicketResponseDTO;
import com.snlbanking.entity.SupportTicket;
import com.snlbanking.entity.User;
import com.snlbanking.exception.ResourceNotFoundException;
import com.snlbanking.repository.SupportTicketRepository;
import com.snlbanking.repository.UserRepository;
import com.snlbanking.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Support Ticket Service Implementation
 * Implements support ticket business logic
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SupportTicketServiceImpl implements SupportTicketService {
  private final SupportTicketRepository supportTicketRepository;
  private final UserRepository userRepository;

  /**
   * Submit a new support ticket
   */
  @Override
  public SupportTicketResponseDTO submitTicket(SupportTicketRequestDTO request) {
    log.info("Submitting support ticket for user: {}", request.getUserId());

    User user = userRepository.findById(request.getUserId())
      .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));

    String ticketNumber = generateTicketNumber();

    SupportTicket ticket = SupportTicket.builder()
      .ticketNumber(ticketNumber)
      .user(user)
      .category(request.getCategory())
      .subject(request.getSubject())
      .description(request.getDescription())
      .status("OPEN")
      .createdAt(LocalDateTime.now())
      .updatedAt(LocalDateTime.now())
      .build();

    SupportTicket savedTicket = supportTicketRepository.save(ticket);
    log.info("Support ticket created with ID: {}", savedTicket.getTicketId());

    return mapToResponseDTO(savedTicket);
  }

  /**
   * Get support ticket by ID
   */
  @Override
  @Transactional(readOnly = true)
  public SupportTicketResponseDTO getTicketById(Long ticketId) {
    log.info("Fetching support ticket with ID: {}", ticketId);

    SupportTicket ticket = supportTicketRepository.findById(ticketId)
      .orElseThrow(() -> new ResourceNotFoundException("Support ticket not found with ID: " + ticketId));

    return mapToResponseDTO(ticket);
  }

  /**
   * Get all support tickets for a user
   */
  @Override
  @Transactional(readOnly = true)
  public Page<SupportTicketResponseDTO> getUserTickets(Long userId, Pageable pageable) {
    log.info("Fetching support tickets for user: {}", userId);

    // Verify user exists
    if (!userRepository.existsById(userId)) {
      throw new ResourceNotFoundException("User not found with ID: " + userId);
    }

    return supportTicketRepository.findByUserId(userId, pageable)
      .map(this::mapToResponseDTO);
  }

  /**
   * Get all support tickets (paginated)
   */
  @Override
  @Transactional(readOnly = true)
  public Page<SupportTicketResponseDTO> getAllTickets(Pageable pageable) {
    log.info("Fetching all support tickets");

    return supportTicketRepository.findAll(pageable)
      .map(this::mapToResponseDTO);
  }

  /**
   * Close a support ticket
   */
  @Override
  public SupportTicketResponseDTO closeTicket(Long ticketId) {
    log.info("Closing support ticket with ID: {}", ticketId);

    SupportTicket ticket = supportTicketRepository.findById(ticketId)
      .orElseThrow(() -> new ResourceNotFoundException("Support ticket not found with ID: " + ticketId));

    ticket.setStatus("CLOSED");
    ticket.setUpdatedAt(LocalDateTime.now());

    SupportTicket updatedTicket = supportTicketRepository.save(ticket);
    log.info("Support ticket closed with ID: {}", ticketId);

    return mapToResponseDTO(updatedTicket);
  }

  /**
   * Get ticket by ticket number
   */
  @Override
  @Transactional(readOnly = true)
  public SupportTicketResponseDTO getTicketByNumber(String ticketNumber) {
    log.info("Fetching support ticket with number: {}", ticketNumber);

    SupportTicket ticket = supportTicketRepository.findByTicketNumber(ticketNumber)
      .orElseThrow(() -> new ResourceNotFoundException("Support ticket not found with number: " + ticketNumber));

    return mapToResponseDTO(ticket);
  }

  /**
   * Generate unique ticket number
   */
  private String generateTicketNumber() {
    return "TICKET" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

  /**
   * Map SupportTicket entity to ResponseDTO
   */
  private SupportTicketResponseDTO mapToResponseDTO(SupportTicket ticket) {
    return SupportTicketResponseDTO.builder()
      .ticketId(ticket.getTicketId())
      .ticketNumber(ticket.getTicketNumber())
      .userId(ticket.getUser().getUserId())
      .userFirstName(ticket.getUser().getFirstName())
      .userLastName(ticket.getUser().getLastName())
      .category(ticket.getCategory())
      .subject(ticket.getSubject())
      .description(ticket.getDescription())
      .status(ticket.getStatus())
      .resolution(ticket.getResolution())
      .createdAt(ticket.getCreatedAt())
      .updatedAt(ticket.getUpdatedAt())
      .build();
  }
}
