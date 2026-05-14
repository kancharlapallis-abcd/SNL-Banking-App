package com.snlbanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snlbanking.dto.SupportTicketRequestDTO;
import com.snlbanking.dto.SupportTicketResponseDTO;
import com.snlbanking.service.SupportTicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupportTicketController.class)
@DisplayName("SupportTicketController Unit Tests")
class SupportTicketControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SupportTicketService supportTicketService;

  @Autowired
  private ObjectMapper objectMapper;

  private SupportTicketRequestDTO testRequestDTO;
  private SupportTicketResponseDTO testResponseDTO;

  @BeforeEach
  void setUp() {
    testRequestDTO = SupportTicketRequestDTO.builder()
      .userId(1L)
      .category("TECHNICAL_ISSUE")
      .subject("Login not working")
      .description("Unable to login to my account")
      .build();

    testResponseDTO = SupportTicketResponseDTO.builder()
      .ticketId(1L)
      .ticketNumber("TICKET123")
      .userId(1L)
      .userFirstName("John")
      .userLastName("Doe")
      .category("TECHNICAL_ISSUE")
      .subject("Login not working")
      .description("Unable to login to my account")
      .status("OPEN")
      .createdAt(LocalDateTime.now())
      .updatedAt(LocalDateTime.now())
      .build();
  }

  @Test
  @DisplayName("Should successfully submit support ticket - POST /api/v1/support/tickets")
  void testSubmitTicket_Success() throws Exception {
    // Arrange
    when(supportTicketService.submitTicket(any(SupportTicketRequestDTO.class)))
      .thenReturn(testResponseDTO);

    // Act & Assert
    mockMvc.perform(post("/api/v1/support/tickets")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testRequestDTO)))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.status").value(201))
      .andExpect(jsonPath("$.data.ticketId").value(1L))
      .andExpect(jsonPath("$.data.status").value("OPEN"));
  }

  @Test
  @DisplayName("Should get ticket by ID - GET /api/v1/support/tickets/{ticketId}")
  void testGetTicketById_Success() throws Exception {
    // Arrange
    when(supportTicketService.getTicketById(1L))
      .thenReturn(testResponseDTO);

    // Act & Assert
    mockMvc.perform(get("/api/v1/support/tickets/1")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value(200))
      .andExpect(jsonPath("$.data.ticketId").value(1L));
  }

  @Test
  @DisplayName("Should get user tickets - GET /api/v1/support/tickets/user/{userId}")
  void testGetUserTickets_Success() throws Exception {
    // Arrange
    Page<SupportTicketResponseDTO> page = new PageImpl<>(
      Arrays.asList(testResponseDTO),
      PageRequest.of(0, 10),
      1
    );
    when(supportTicketService.getUserTickets(eq(1L), any()))
      .thenReturn(page);

    // Act & Assert
    mockMvc.perform(get("/api/v1/support/tickets/user/1?page=0&size=10")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value(200))
      .andExpect(jsonPath("$.data.content[0].ticketId").value(1L));
  }

  @Test
  @DisplayName("Should get all tickets - GET /api/v1/support/tickets")
  void testGetAllTickets_Success() throws Exception {
    // Arrange
    Page<SupportTicketResponseDTO> page = new PageImpl<>(
      Arrays.asList(testResponseDTO),
      PageRequest.of(0, 10),
      1
    );
    when(supportTicketService.getAllTickets(any()))
      .thenReturn(page);

    // Act & Assert
    mockMvc.perform(get("/api/v1/support/tickets?page=0&size=10")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value(200))
      .andExpect(jsonPath("$.data.content[0].ticketId").value(1L));
  }

  @Test
  @DisplayName("Should close ticket - POST /api/v1/support/tickets/{ticketId}/close")
  void testCloseTicket_Success() throws Exception {
    // Arrange
    testResponseDTO.setStatus("CLOSED");
    when(supportTicketService.closeTicket(1L))
      .thenReturn(testResponseDTO);

    // Act & Assert
    mockMvc.perform(post("/api/v1/support/tickets/1/close")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.status").value("CLOSED"));
  }

  @Test
  @DisplayName("Should get ticket by number - GET /api/v1/support/tickets/number/{ticketNumber}")
  void testGetTicketByNumber_Success() throws Exception {
    // Arrange
    when(supportTicketService.getTicketByNumber("TICKET123"))
      .thenReturn(testResponseDTO);

    // Act & Assert
    mockMvc.perform(get("/api/v1/support/tickets/number/TICKET123")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.ticketNumber").value("TICKET123"));
  }

  @Test
  @DisplayName("Should return bad request for missing required fields")
  void testSubmitTicket_MissingFields() throws Exception {
    // Arrange
    String invalidJson = "{\"userId\": 1}";

    // Act & Assert
    mockMvc.perform(post("/api/v1/support/tickets")
        .contentType(MediaType.APPLICATION_JSON)
        .content(invalidJson))
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should return bad request for invalid subject length")
  void testSubmitTicket_InvalidSubjectLength() throws Exception {
    // Arrange
    testRequestDTO.setSubject("Hi"); // Too short

    // Act & Assert
    mockMvc.perform(post("/api/v1/support/tickets")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testRequestDTO)))
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should return bad request for invalid description length")
  void testSubmitTicket_InvalidDescriptionLength() throws Exception {
    // Arrange
    testRequestDTO.setDescription("Short"); // Too short

    // Act & Assert
    mockMvc.perform(post("/api/v1/support/tickets")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testRequestDTO)))
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should return bad request for empty category")
  void testSubmitTicket_EmptyCategory() throws Exception {
    // Arrange
    testRequestDTO.setCategory("");

    // Act & Assert
    mockMvc.perform(post("/api/v1/support/tickets")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testRequestDTO)))
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should support all ticket categories")
  void testSubmitTicket_DifferentCategories() throws Exception {
    String[] categories = {"GENERAL", "TECHNICAL_ISSUE", "ACCOUNT_PROBLEM", "TRANSACTION_ISSUE", "COMPLAINT"};

    for (String category : categories) {
      // Arrange
      testRequestDTO.setCategory(category);
      testResponseDTO.setCategory(category);
      when(supportTicketService.submitTicket(any(SupportTicketRequestDTO.class)))
        .thenReturn(testResponseDTO);

      // Act & Assert
      mockMvc.perform(post("/api/v1/support/tickets")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(testRequestDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.category").value(category));
    }
  }

  @Test
  @DisplayName("Should handle pagination with different page sizes")
  void testGetAllTickets_DifferentPageSizes() throws Exception {
    // Arrange
    Page<SupportTicketResponseDTO> page = new PageImpl<>(
      Arrays.asList(testResponseDTO),
      PageRequest.of(0, 20),
      1
    );
    when(supportTicketService.getAllTickets(any()))
      .thenReturn(page);

    // Act & Assert
    mockMvc.perform(get("/api/v1/support/tickets?page=0&size=20")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value(200));
  }
}
