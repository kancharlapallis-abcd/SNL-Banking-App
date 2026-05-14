package com.snlbanking.service;

import com.snlbanking.dto.BillPaymentRequestDTO;
import com.snlbanking.dto.BillPaymentResponseDTO;
import com.snlbanking.entity.Account;
import com.snlbanking.entity.BillPayment;
import com.snlbanking.entity.User;
import com.snlbanking.exception.InsufficientBalanceException;
import com.snlbanking.exception.InvalidOperationException;
import com.snlbanking.exception.ResourceNotFoundException;
import com.snlbanking.repository.AccountRepository;
import com.snlbanking.repository.BillPaymentRepository;
import com.snlbanking.service.impl.BillPaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BillPaymentService Unit Tests")
class BillPaymentServiceTest {
  @Mock
  private BillPaymentRepository billPaymentRepository;

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private BillPaymentServiceImpl billPaymentService;

  private User testUser;
  private Account testAccount;
  private BillPayment testBillPayment;
  private BillPaymentRequestDTO testRequestDTO;

  @BeforeEach
  void setUp() {
    testUser = User.builder()
      .userId(1L)
      .firstName("John")
      .lastName("Doe")
      .email("john@example.com")
      .build();

    testAccount = Account.builder()
      .accountId(1L)
      .accountNumber("ACC001")
      .user(testUser)
      .balance(BigDecimal.valueOf(10000))
      .status("ACTIVE")
      .build();

    testBillPayment = BillPayment.builder()
      .billPaymentId(1L)
      .billReference("BILL123")
      .account(testAccount)
      .billType("ELECTRICITY")
      .billerName("Power Company")
      .billAmount(BigDecimal.valueOf(500))
      .status("COMPLETED")
      .build();

    testRequestDTO = BillPaymentRequestDTO.builder()
      .accountId(1L)
      .billType("ELECTRICITY")
      .billerName("Power Company")
      .billAmount(BigDecimal.valueOf(500))
      .build();
  }

  @Test
  @DisplayName("Should successfully pay bill when account has sufficient balance")
  void testPayBill_Success() {
    // Arrange
    when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
    when(billPaymentRepository.save(any(BillPayment.class))).thenReturn(testBillPayment);

    // Act
    BillPaymentResponseDTO result = billPaymentService.payBill(testRequestDTO);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getBillPaymentId()).isEqualTo(1L);
    assertThat(result.getBillAmount()).isEqualTo(BigDecimal.valueOf(500));
    assertThat(result.getStatus()).isEqualTo("COMPLETED");

    // Verify
    verify(accountRepository, times(1)).findById(1L);
    verify(billPaymentRepository, times(1)).save(any(BillPayment.class));
  }

  @Test
  @DisplayName("Should throw InsufficientBalanceException when account balance is insufficient")
  void testPayBill_InsufficientBalance() {
    // Arrange
    testAccount.setBalance(BigDecimal.valueOf(100));
    when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

    // Act & Assert
    assertThatThrownBy(() -> billPaymentService.payBill(testRequestDTO))
      .isInstanceOf(InsufficientBalanceException.class)
      .hasMessageContaining("Insufficient balance");

    verify(billPaymentRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should throw ResourceNotFoundException when account not found")
  void testPayBill_AccountNotFound() {
    // Arrange
    when(accountRepository.findById(999L)).thenReturn(Optional.empty());

    BillPaymentRequestDTO invalidRequest = BillPaymentRequestDTO.builder()
      .accountId(999L)
      .billType("ELECTRICITY")
      .billerName("Power Company")
      .billAmount(BigDecimal.valueOf(500))
      .build();

    // Act & Assert
    assertThatThrownBy(() -> billPaymentService.payBill(invalidRequest))
      .isInstanceOf(ResourceNotFoundException.class)
      .hasMessageContaining("Account not found");

    verify(billPaymentRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should get bill payment by ID successfully")
  void testGetBillPaymentById_Success() {
    // Arrange
    when(billPaymentRepository.findById(1L)).thenReturn(Optional.of(testBillPayment));

    // Act
    BillPaymentResponseDTO result = billPaymentService.getBillPaymentById(1L);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getBillPaymentId()).isEqualTo(1L);
    verify(billPaymentRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Should throw ResourceNotFoundException when bill payment not found")
  void testGetBillPaymentById_NotFound() {
    // Arrange
    when(billPaymentRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> billPaymentService.getBillPaymentById(999L))
      .isInstanceOf(ResourceNotFoundException.class)
      .hasMessageContaining("Bill payment not found");
  }

  @Test
  @DisplayName("Should retrieve all bill payments for an account with pagination")
  void testGetAccountBillPayments_Success() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    Page<BillPayment> billPaymentPage = new PageImpl<>(Arrays.asList(testBillPayment), pageable, 1);
    when(accountRepository.existsById(1L)).thenReturn(true);
    when(billPaymentRepository.findByAccountId(1L, pageable)).thenReturn(billPaymentPage);

    // Act
    Page<BillPaymentResponseDTO> result = billPaymentService.getAccountBillPayments(1L, pageable);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getTotalElements()).isEqualTo(1);
    verify(billPaymentRepository, times(1)).findByAccountId(1L, pageable);
  }

  @Test
  @DisplayName("Should retrieve all bill payments with pagination")
  void testGetAllBillPayments_Success() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    Page<BillPayment> billPaymentPage = new PageImpl<>(Arrays.asList(testBillPayment), pageable, 1);
    when(billPaymentRepository.findAll(pageable)).thenReturn(billPaymentPage);

    // Act
    Page<BillPaymentResponseDTO> result = billPaymentService.getAllBillPayments(pageable);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getContent()).hasSize(1);
    verify(billPaymentRepository, times(1)).findAll(pageable);
  }

  @Test
  @DisplayName("Should successfully cancel bill payment and refund balance")
  void testCancelBillPayment_Success() {
    // Arrange
    testBillPayment.setStatus("COMPLETED");
    when(billPaymentRepository.findById(1L)).thenReturn(Optional.of(testBillPayment));
    when(billPaymentRepository.save(any(BillPayment.class))).thenReturn(testBillPayment);

    // Act
    BillPaymentResponseDTO result = billPaymentService.cancelBillPayment(1L);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getStatus()).isEqualTo("CANCELLED");
    verify(billPaymentRepository, times(1)).save(any(BillPayment.class));
  }

  @Test
  @DisplayName("Should throw exception when cancelling already cancelled bill")
  void testCancelBillPayment_AlreadyCancelled() {
    // Arrange
    testBillPayment.setStatus("CANCELLED");
    when(billPaymentRepository.findById(1L)).thenReturn(Optional.of(testBillPayment));

    // Act & Assert
    assertThatThrownBy(() -> billPaymentService.cancelBillPayment(1L))
      .isInstanceOf(InvalidOperationException.class);

    verify(billPaymentRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should get bill payment by reference number")
  void testGetBillPaymentByReference_Success() {
    // Arrange
    when(billPaymentRepository.findByBillReference("BILL123")).thenReturn(Optional.of(testBillPayment));

    // Act
    BillPaymentResponseDTO result = billPaymentService.getBillPaymentByReference("BILL123");

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getBillReference()).isEqualTo("BILL123");
    verify(billPaymentRepository, times(1)).findByBillReference("BILL123");
  }

  @Test
  @DisplayName("Should throw exception for non-existent bill reference")
  void testGetBillPaymentByReference_NotFound() {
    // Arrange
    when(billPaymentRepository.findByBillReference("INVALID")).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> billPaymentService.getBillPaymentByReference("INVALID"))
      .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should handle edge case of zero bill amount")
  void testPayBill_ZeroAmount() {
    // Arrange
    BillPaymentRequestDTO zeroAmountRequest = BillPaymentRequestDTO.builder()
      .accountId(1L)
      .billType("ELECTRICITY")
      .billerName("Power Company")
      .billAmount(BigDecimal.ZERO)
      .build();

    when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

    // Act & Assert
    assertThatThrownBy(() -> billPaymentService.payBill(zeroAmountRequest))
      .isInstanceOf(InvalidOperationException.class);
  }

  @Test
  @DisplayName("Should handle edge case of negative bill amount")
  void testPayBill_NegativeAmount() {
    // Arrange
    BillPaymentRequestDTO negativeAmountRequest = BillPaymentRequestDTO.builder()
      .accountId(1L)
      .billType("ELECTRICITY")
      .billerName("Power Company")
      .billAmount(BigDecimal.valueOf(-100))
      .build();

    when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

    // Act & Assert
    assertThatThrownBy(() -> billPaymentService.payBill(negativeAmountRequest))
      .isInstanceOf(InvalidOperationException.class);
  }
}
