package com.snlbanking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Support Ticket Request DTO
 * Validates incoming support ticket requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportTicketRequestDTO {
  @NotNull(message = "User ID is required")
  private Long userId;

  @NotBlank(message = "Category is required")
  @Size(max = 100, message = "Category cannot exceed 100 characters")
  private String category;

  @NotBlank(message = "Subject is required")
  @Size(min = 5, max = 200, message = "Subject must be between 5 and 200 characters")
  private String subject;

  @NotBlank(message = "Description is required")
  @Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
  private String description;
}
