package com.snlbanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snlbanking.dto.LoanApplicationRequestDTO;
import com.snlbanking.dto.LoanApplicationResponseDTO;
import com.snlbanking.service.LoanService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
@DisplayName("LoanController Unit Tests")
class LoanControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LoanService loanService;

  @Autowired
  private ObjectMapper objectMapper;

  private LoanApplicationRequestDTO testRequestDTO;
  private LoanApplicationResponseDTO testResponseDTO;

  @BeforeEach
  void setUp() {
    testRequestDTO = LoanApplicationRequestDTO.builder()
      .userId(1L)
      .loanType("PERSONAL_LOAN")
      .principalAmount(BigDecimal.valueOf(100000))
      .interestRate(BigDecimal.valueOf(8.5))
      .tenureMonths(60)
      .build();

    testResponseDTO = LoanApplicationResponseDTO.builder()
      .loanApplicationId(1L)
      .loanNumber("LOAN123")
      .userId(1L)
      .userFirstName("John")
      .userLastName("Doe")
      .loanType("PERSONAL_LOAN")
      .principalAmount(BigDecimal.valueOf(100000))
      .interestRate(BigDecimal.valueOf(8.5))
      .tenureMonths(60)
      .emiAmount(BigDecimal.valueOf(2000))
      .remainingAmount(BigDecimal.valueOf(100000))
      .status("PENDING")
      .createdAt(LocalDateTime.now())
      .updatedAt(LocalDateTime.now())
      .build();
  }

  @Test
  @DisplayName("Should successfully apply for loan - POST /api/v1/loans/apply")
  void testApplyLoan_Success() throws Exception {
    // Arrange
    when(loanService.submitLoanApplication(any(LoanApplicationRequestDTO.class)))
      .thenReturn(testResponseDTO);

    // Act & Assert
    mockMvc.perform(post("/api/v1/loans/apply")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testRequestDTO)))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.status").value(201))
      .andExpect(jsonPath("$.data.loanApplicationId").value(1L))
      .andExpect(jsonPath("$.data.status").value("PENDING"));
  }

  @Test
  @DisplayName("Should get loan by ID - GET /api/v1/loans/{loanId}")
  void testGetLoanById_Success() throws Exception {
    // Arrange
    when(loanService.getLoanById(1L))
      .thenReturn(testResponseDTO);

    // Act & Assert
    mockMvc.perform(get("/api/v1/loans/1")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value(200))
      .andExpect(jsonPath("$.data.loanApplicationId").value(1L));
  }

  @Test
  @DisplayName("Should get user loans - GET /api/v1/loans/user/{userId}")
  void testGetUserLoans_Success() throws Exception {
    // Arrange
    Page<LoanApplicationResponseDTO> page = new PageImpl<>(
      Arrays.asList(testResponseDTO),
      PageRequest.of(0, 10),
      1
    );
    when(loanService.getUserLoans(eq(1L), any()))
      .thenReturn(page);

    // Act & Assert
    mockMvc.perform(get("/api/v1/loans/user/1?page=0&size=10")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value(200))
      .andExpect(jsonPath("$.data.content[0].loanApplicationId").value(1L));
  }

  @Test
  @DisplayName("Should get all loans - GET /api/v1/loans")
  void testGetAllLoans_Success() throws Exception {
    // Arrange
    Page<LoanApplicationResponseDTO> page = new PageImpl<>(
      Arrays.asList(testResponseDTO),
      PageRequest.of(0, 10),
      1
    );
    when(loanService.getAllLoans(any()))
      .thenReturn(page);

    // Act & Assert
    mockMvc.perform(get("/api/v1/loans?page=0&size=10")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value(200))
      .andExpect(jsonPath("$.data.content[0].loanApplicationId").value(1L));
  }

  @Test
  @DisplayName("Should approve loan - POST /api/v1/loans/{loanId}/approve")
  void testApproveLoan_Success() throws Exception {
    // Arrange
    testResponseDTO.setStatus("ACTIVE");
    when(loanService.approveLoan(1L))
      .thenReturn(testResponseDTO);

    // Act & Assert
    mockMvc.perform(post("/api/v1/loans/1/approve")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.status").value("ACTIVE"));
  }

  @Test
  @DisplayName("Should reject loan - POST /api/v1/loans/{loanId}/reject")
  void testRejectLoan_Success() throws Exception {
    // Arrange
    testResponseDTO.setStatus("REJECTED");
    when(loanService.rejectLoan(1L))
      .thenReturn(testResponseDTO);

    // Act & Assert
    mockMvc.perform(post("/api/v1/loans/1/reject")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.status").value("REJECTED"));
  }

  @Test
  @DisplayName("Should get loan by number - GET /api/v1/loans/number/{loanNumber}")
  void testGetLoanByNumber_Success() throws Exception {
    // Arrange
    when(loanService.getLoanByNumber("LOAN123"))
      .thenReturn(testResponseDTO);

    // Act & Assert
    mockMvc.perform(get("/api/v1/loans/number/LOAN123")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.loanNumber").value("LOAN123"));
  }

  @Test
  @DisplayName("Should return bad request for invalid principal amount")
  void testApplyLoan_InvalidPrincipal() throws Exception {
    // Arrange
    testRequestDTO.setPrincipalAmount(BigDecimal.ZERO);

    // Act & Assert
    mockMvc.perform(post("/api/v1/loans/apply")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testRequestDTO)))
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should return bad request for invalid tenure")
  void testApplyLoan_InvalidTenure() throws Exception {
    // Arrange
    testRequestDTO.setTenureMonths(3); // Less than minimum 6 months

    // Act & Assert
    mockMvc.perform(post("/api/v1/loans/apply")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testRequestDTO)))
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should return bad request for invalid interest rate")
  void testApplyLoan_InvalidInterestRate() throws Exception {
    // Arrange
    testRequestDTO.setInterestRate(BigDecimal.valueOf(-5));

    // Act & Assert
    mockMvc.perform(post("/api/v1/loans/apply")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testRequestDTO)))
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should return bad request for missing required fields")
  void testApplyLoan_MissingFields() throws Exception {
    // Arrange
    String invalidJson = "{\"userId\": 1}";

    // Act & Assert
    mockMvc.perform(post("/api/v1/loans/apply")
        .contentType(MediaType.APPLICATION_JSON)
        .content(invalidJson))
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should support all loan types")
  void testApplyLoan_DifferentLoanTypes() throws Exception {
    String[] loanTypes = {"PERSONAL_LOAN", "HOME_LOAN", "CAR_LOAN", "EDUCATION_LOAN"};

    for (String loanType : loanTypes) {
      // Arrange
      testRequestDTO.setLoanType(loanType);
      testResponseDTO.setLoanType(loanType);
      when(loanService.submitLoanApplication(any(LoanApplicationRequestDTO.class)))
        .thenReturn(testResponseDTO);

      // Act & Assert
      mockMvc.perform(post("/api/v1/loans/apply")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(testRequestDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.loanType").value(loanType));
    }
  }
}
