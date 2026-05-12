package com.snlbanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Support Ticket Response DTO
 * Returns support ticket information to frontend
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportTicketResponseDTO {
  private Long ticketId;
  private String ticketNumber;
  private Long userId;
  private String userFirstName;
  private String userLastName;
  private String category;
  private String subject;
  private String description;
  private String status;
  private String resolution;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
