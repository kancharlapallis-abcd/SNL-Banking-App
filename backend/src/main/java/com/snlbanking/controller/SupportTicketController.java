package com.snlbanking.controller;

import com.snlbanking.dto.APIResponseDTO;
import com.snlbanking.dto.SupportTicketRequestDTO;
import com.snlbanking.dto.SupportTicketResponseDTO;
import com.snlbanking.service.SupportTicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Support Ticket REST Controller
 * Handles support ticket related API endpoints
 */
@RestController
@RequestMapping("/v1/support/tickets")
@RequiredArgsConstructor
@Slf4j
public class SupportTicketController {
  private final SupportTicketService supportTicketService;

  /**
   * Submit a new support ticket
   * POST /api/v1/support/tickets
   */
  @PostMapping
  public ResponseEntity<APIResponseDTO<SupportTicketResponseDTO>> submitTicket(
    @Valid @RequestBody SupportTicketRequestDTO request) {
    log.info("Submitting support ticket for user: {}", request.getUserId());

    SupportTicketResponseDTO response = supportTicketService.submitTicket(request);

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(APIResponseDTO.<SupportTicketResponseDTO>builder()
        .status(HttpStatus.CREATED.value())
        .message("Support ticket submitted successfully")
        .data(response)
        .build());
  }

  /**
   * Get support ticket by ID
   * GET /api/v1/support/tickets/{ticketId}
   */
  @GetMapping("/{ticketId}")
  public ResponseEntity<APIResponseDTO<SupportTicketResponseDTO>> getTicketById(
    @PathVariable Long ticketId) {
    log.info("Fetching support ticket with ID: {}", ticketId);

    SupportTicketResponseDTO response = supportTicketService.getTicketById(ticketId);

    return ResponseEntity.ok(APIResponseDTO.<SupportTicketResponseDTO>builder()
      .status(HttpStatus.OK.value())
      .message("Support ticket retrieved successfully")
      .data(response)
      .build());
  }

  /**
   * Get all support tickets for a user
   * GET /api/v1/support/tickets/user/{userId}
   */
  @GetMapping("/user/{userId}")
  public ResponseEntity<APIResponseDTO<Page<SupportTicketResponseDTO>>> getUserTickets(
    @PathVariable Long userId,
    Pageable pageable) {
    log.info("Fetching support tickets for user: {}", userId);

    Page<SupportTicketResponseDTO> response = supportTicketService.getUserTickets(userId, pageable);

    return ResponseEntity.ok(APIResponseDTO.<Page<SupportTicketResponseDTO>>builder()
      .status(HttpStatus.OK.value())
      .message("User support tickets retrieved successfully")
      .data(response)
      .build());
  }

  /**
   * Get all support tickets (paginated)
   * GET /api/v1/support/tickets
   */
  @GetMapping
  public ResponseEntity<APIResponseDTO<Page<SupportTicketResponseDTO>>> getAllTickets(
    Pageable pageable) {
    log.info("Fetching all support tickets");

    Page<SupportTicketResponseDTO> response = supportTicketService.getAllTickets(pageable);

    return ResponseEntity.ok(APIResponseDTO.<Page<SupportTicketResponseDTO>>builder()
      .status(HttpStatus.OK.value())
      .message("All support tickets retrieved successfully")
      .data(response)
      .build());
  }

  /**
   * Close a support ticket
   * POST /api/v1/support/tickets/{ticketId}/close
   */
  @PostMapping("/{ticketId}/close")
  public ResponseEntity<APIResponseDTO<SupportTicketResponseDTO>> closeTicket(
    @PathVariable Long ticketId) {
    log.info("Closing support ticket with ID: {}", ticketId);

    SupportTicketResponseDTO response = supportTicketService.closeTicket(ticketId);

    return ResponseEntity.ok(APIResponseDTO.<SupportTicketResponseDTO>builder()
      .status(HttpStatus.OK.value())
      .message("Support ticket closed successfully")
      .data(response)
      .build());
  }

  /**
   * Get support ticket by ticket number
   * GET /api/v1/support/tickets/number/{ticketNumber}
   */
  @GetMapping("/number/{ticketNumber}")
  public ResponseEntity<APIResponseDTO<SupportTicketResponseDTO>> getTicketByNumber(
    @PathVariable String ticketNumber) {
    log.info("Fetching support ticket with number: {}", ticketNumber);

    SupportTicketResponseDTO response = supportTicketService.getTicketByNumber(ticketNumber);

    return ResponseEntity.ok(APIResponseDTO.<SupportTicketResponseDTO>builder()
      .status(HttpStatus.OK.value())
      .message("Support ticket retrieved successfully")
      .data(response)
      .build());
  }
}
