package com.snlbanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snlbanking.dto.BillPaymentRequestDTO;
import com.snlbanking.dto.BillPaymentResponseDTO;
import com.snlbanking.service.BillPaymentService;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BillPaymentController.class)
@DisplayName("BillPaymentController Unit Tests")
class BillPaymentControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BillPaymentService billPaymentService;

  @Autowired
  private ObjectMapper objectMapper;

  private BillPaymentRequestDTO testRequestDTO;
  private BillPaymentResponseDTO testResponseDTO;

  @BeforeEach
  void setUp() {
    testRequestDTO = BillPaymentRequestDTO.builder()
      .accountId(1L)
      .billType("ELECTRICITY")
      .billerName("Power Company")
      .billAmount(BigDecimal.valueOf(500))
      .build();

    testResponseDTO = BillPaymentResponseDTO.builder()
      .billPaymentId(1L)
      .billReference("BILL123")
      .accountId(1L)
      .accountNumber("ACC001")
      .billType("ELECTRICITY")
      .billerName("Power Company")
      .billAmount(BigDecimal.valueOf(500))
      .status("COMPLETED")
      .createdAt(LocalDateTime.now())
      .updatedAt(LocalDateTime.now())
      .build();
  }

  @Test
  @DisplayName("Should successfully pay bill - POST /api/v1/bills/pay")
  void testPayBill_Success() throws Exception {
    // Arrange
    when(billPaymentService.payBill(any(BillPaymentRequestDTO.class)))
      .thenReturn(testResponseDTO);

    // Act & Assert
    mockMvc.perform(post("/api/v1/bills/pay")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testRequestDTO)))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.status").value(201))
      .andExpect(jsonPath("$.data.billPaymentId").value(1L))
      .andExpect(jsonPath("$.data.status").value("COMPLETED"));
  }

  @Test
  @DisplayName("Should get bill payment by ID - GET /api/v1/bills/{billPaymentId}")
  void testGetBillPaymentById_Success() throws Exception {
    // Arrange
    when(billPaymentService.getBillPaymentById(1L))
      .thenReturn(testResponseDTO);

    // Act & Assert
    mockMvc.perform(get("/api/v1/bills/1")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value(200))
      .andExpect(jsonPath("$.data.billPaymentId").value(1L));
  }

  @Test
  @DisplayName("Should get all bill payments for account - GET /api/v1/bills/account/{accountId}")
  void testGetAccountBillPayments_Success() throws Exception {
    // Arrange
    Page<BillPaymentResponseDTO> page = new PageImpl<>(
      Arrays.asList(testResponseDTO),
      PageRequest.of(0, 10),
      1
    );
    when(billPaymentService.getAccountBillPayments(eq(1L), any()))
      .thenReturn(page);

    // Act & Assert
    mockMvc.perform(get("/api/v1/bills/account/1?page=0&size=10")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value(200))
      .andExpect(jsonPath("$.data.content[0].billPaymentId").value(1L));
  }

  @Test
  @DisplayName("Should get all bill payments - GET /api/v1/bills")
  void testGetAllBillPayments_Success() throws Exception {
    // Arrange
    Page<BillPaymentResponseDTO> page = new PageImpl<>(
      Arrays.asList(testResponseDTO),
      PageRequest.of(0, 10),
      1
    );
    when(billPaymentService.getAllBillPayments(any()))
      .thenReturn(page);

    // Act & Assert
    mockMvc.perform(get("/api/v1/bills?page=0&size=10")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value(200))
      .andExpect(jsonPath("$.data.content[0].billPaymentId").value(1L));
  }

  @Test
  @DisplayName("Should cancel bill payment - POST /api/v1/bills/{billPaymentId}/cancel")
  void testCancelBillPayment_Success() throws Exception {
    // Arrange
    testResponseDTO.setStatus("CANCELLED");
    when(billPaymentService.cancelBillPayment(1L))
      .thenReturn(testResponseDTO);

    // Act & Assert
    mockMvc.perform(post("/api/v1/bills/1/cancel")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.status").value("CANCELLED"));
  }

  @Test
  @DisplayName("Should get bill payment by reference - GET /api/v1/bills/reference/{billReference}")
  void testGetBillPaymentByReference_Success() throws Exception {
    // Arrange
    when(billPaymentService.getBillPaymentByReference("BILL123"))
      .thenReturn(testResponseDTO);

    // Act & Assert
    mockMvc.perform(get("/api/v1/bills/reference/BILL123")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.billReference").value("BILL123"));
  }

  @Test
  @DisplayName("Should return bad request for invalid bill amount")
  void testPayBill_InvalidAmount() throws Exception {
    // Arrange
    BillPaymentRequestDTO invalidRequest = BillPaymentRequestDTO.builder()
      .accountId(1L)
      .billType("ELECTRICITY")
      .billerName("Power Company")
      .billAmount(BigDecimal.valueOf(-100)) // Invalid negative amount
      .build();

    // Act & Assert
    mockMvc.perform(post("/api/v1/bills/pay")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidRequest)))
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should return bad request for missing required fields")
  void testPayBill_MissingFields() throws Exception {
    // Arrange
    String invalidJson = "{\"accountId\": 1}"; // Missing required fields

    // Act & Assert
    mockMvc.perform(post("/api/v1/bills/pay")
        .contentType(MediaType.APPLICATION_JSON)
        .content(invalidJson))
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should handle empty bill type gracefully")
  void testPayBill_EmptyBillType() throws Exception {
    // Arrange
    testRequestDTO.setBillType("");

    // Act & Assert
    mockMvc.perform(post("/api/v1/bills/pay")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testRequestDTO)))
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should handle pagination with different page sizes")
  void testGetAllBillPayments_DifferentPageSizes() throws Exception {
    // Arrange
    Page<BillPaymentResponseDTO> page = new PageImpl<>(
      Arrays.asList(testResponseDTO),
      PageRequest.of(0, 20),
      1
    );
    when(billPaymentService.getAllBillPayments(any()))
      .thenReturn(page);

    // Act & Assert
    mockMvc.perform(get("/api/v1/bills?page=0&size=20")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value(200));
  }
}
