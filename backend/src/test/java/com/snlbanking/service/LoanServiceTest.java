package com.snlbanking.service;

import com.snlbanking.dto.LoanApplicationRequestDTO;
import com.snlbanking.dto.LoanApplicationResponseDTO;
import com.snlbanking.entity.Loan;
import com.snlbanking.entity.User;
import com.snlbanking.exception.ResourceNotFoundException;
import com.snlbanking.repository.LoanRepository;
import com.snlbanking.repository.UserRepository;
import com.snlbanking.service.impl.LoanServiceImpl;
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
@DisplayName("LoanService Unit Tests")
class LoanServiceTest {
  @Mock
  private LoanRepository loanRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private LoanServiceImpl loanService;

  private User testUser;
  private Loan testLoan;
  private LoanApplicationRequestDTO testRequestDTO;

  @BeforeEach
  void setUp() {
    testUser = User.builder()
      .userId(1L)
      .firstName("John")
      .lastName("Doe")
      .email("john@example.com")
      .build();

    testLoan = Loan.builder()
      .loanId(1L)
      .loanNumber("LOAN123")
      .user(testUser)
      .loanType("PERSONAL_LOAN")
      .principalAmount(BigDecimal.valueOf(100000))
      .interestRate(BigDecimal.valueOf(8.5))
      .tenureMonths(60)
      .emiAmount(BigDecimal.valueOf(2000))
      .remainingAmount(BigDecimal.valueOf(100000))
      .status("PENDING")
      .build();

    testRequestDTO = LoanApplicationRequestDTO.builder()
      .userId(1L)
      .loanType("PERSONAL_LOAN")
      .principalAmount(BigDecimal.valueOf(100000))
      .interestRate(BigDecimal.valueOf(8.5))
      .tenureMonths(60)
      .build();
  }

  @Test
  @DisplayName("Should successfully submit loan application with PENDING status")
  void testSubmitLoanApplication_Success() {
    // Arrange
    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    when(loanRepository.save(any(Loan.class))).thenReturn(testLoan);

    // Act
    LoanApplicationResponseDTO result = loanService.submitLoanApplication(testRequestDTO);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getLoanApplicationId()).isEqualTo(1L);
    assertThat(result.getStatus()).isEqualTo("PENDING");
    assertThat(result.getPrincipalAmount()).isEqualTo(BigDecimal.valueOf(100000));
    assertThat(result.getEmiAmount()).isNotNull();

    verify(userRepository, times(1)).findById(1L);
    verify(loanRepository, times(1)).save(any(Loan.class));
  }

  @Test
  @DisplayName("Should calculate EMI correctly")
  void testSubmitLoanApplication_EMICalculation() {
    // Arrange: EMI = [P * R * (1 + R)^N] / [(1 + R)^N - 1]
    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    when(loanRepository.save(any(Loan.class))).thenReturn(testLoan);

    // Act
    LoanApplicationResponseDTO result = loanService.submitLoanApplication(testRequestDTO);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getEmiAmount()).isGreaterThan(BigDecimal.ZERO);
    // For 100000 at 8.5% for 60 months, EMI should be around 2000+
    assertThat(result.getEmiAmount()).isGreaterThan(BigDecimal.valueOf(1500));
  }

  @Test
  @DisplayName("Should throw ResourceNotFoundException when user not found")
  void testSubmitLoanApplication_UserNotFound() {
    // Arrange
    when(userRepository.findById(999L)).thenReturn(Optional.empty());

    LoanApplicationRequestDTO invalidRequest = LoanApplicationRequestDTO.builder()
      .userId(999L)
      .loanType("PERSONAL_LOAN")
      .principalAmount(BigDecimal.valueOf(100000))
      .interestRate(BigDecimal.valueOf(8.5))
      .tenureMonths(60)
      .build();

    // Act & Assert
    assertThatThrownBy(() -> loanService.submitLoanApplication(invalidRequest))
      .isInstanceOf(ResourceNotFoundException.class)
      .hasMessageContaining("User not found");

    verify(loanRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should get loan by ID successfully")
  void testGetLoanById_Success() {
    // Arrange
    when(loanRepository.findById(1L)).thenReturn(Optional.of(testLoan));

    // Act
    LoanApplicationResponseDTO result = loanService.getLoanById(1L);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getLoanApplicationId()).isEqualTo(1L);
    assertThat(result.getLoanNumber()).isEqualTo("LOAN123");
    verify(loanRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Should throw ResourceNotFoundException when loan not found")
  void testGetLoanById_NotFound() {
    // Arrange
    when(loanRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> loanService.getLoanById(999L))
      .isInstanceOf(ResourceNotFoundException.class)
      .hasMessageContaining("Loan not found");
  }

  @Test
  @DisplayName("Should retrieve all loans for a user with pagination")
  void testGetUserLoans_Success() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    Page<Loan> loanPage = new PageImpl<>(Arrays.asList(testLoan), pageable, 1);
    when(userRepository.existsById(1L)).thenReturn(true);
    when(loanRepository.findByUserId(1L, pageable)).thenReturn(loanPage);

    // Act
    Page<LoanApplicationResponseDTO> result = loanService.getUserLoans(1L, pageable);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getTotalElements()).isEqualTo(1);
    verify(loanRepository, times(1)).findByUserId(1L, pageable);
  }

  @Test
  @DisplayName("Should retrieve all loans with pagination")
  void testGetAllLoans_Success() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    Page<Loan> loanPage = new PageImpl<>(Arrays.asList(testLoan), pageable, 1);
    when(loanRepository.findAll(pageable)).thenReturn(loanPage);

    // Act
    Page<LoanApplicationResponseDTO> result = loanService.getAllLoans(pageable);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getContent()).hasSize(1);
    verify(loanRepository, times(1)).findAll(pageable);
  }

  @Test
  @DisplayName("Should successfully approve loan and change status to ACTIVE")
  void testApproveLoan_Success() {
    // Arrange
    testLoan.setStatus("PENDING");
    when(loanRepository.findById(1L)).thenReturn(Optional.of(testLoan));
    when(loanRepository.save(any(Loan.class))).thenReturn(testLoan);

    // Act
    LoanApplicationResponseDTO result = loanService.approveLoan(1L);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getStatus()).isEqualTo("ACTIVE");
    verify(loanRepository, times(1)).save(any(Loan.class));
  }

  @Test
  @DisplayName("Should successfully reject loan and change status to REJECTED")
  void testRejectLoan_Success() {
    // Arrange
    testLoan.setStatus("PENDING");
    when(loanRepository.findById(1L)).thenReturn(Optional.of(testLoan));
    when(loanRepository.save(any(Loan.class))).thenReturn(testLoan);

    // Act
    LoanApplicationResponseDTO result = loanService.rejectLoan(1L);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getStatus()).isEqualTo("REJECTED");
    verify(loanRepository, times(1)).save(any(Loan.class));
  }

  @Test
  @DisplayName("Should support all loan types")
  void testSubmitLoanApplication_DifferentLoanTypes() {
    // Arrange
    String[] loanTypes = {"PERSONAL_LOAN", "HOME_LOAN", "CAR_LOAN", "EDUCATION_LOAN"};

    for (String loanType : loanTypes) {
      Loan loan = Loan.builder()
        .loanId(1L)
        .loanNumber("LOAN123")
        .user(testUser)
        .loanType(loanType)
        .principalAmount(BigDecimal.valueOf(100000))
        .interestRate(BigDecimal.valueOf(8.5))
        .tenureMonths(60)
        .emiAmount(BigDecimal.valueOf(2000))
        .status("PENDING")
        .build();

      when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
      when(loanRepository.save(any(Loan.class))).thenReturn(loan);

      LoanApplicationRequestDTO request = LoanApplicationRequestDTO.builder()
        .userId(1L)
        .loanType(loanType)
        .principalAmount(BigDecimal.valueOf(100000))
        .interestRate(BigDecimal.valueOf(8.5))
        .tenureMonths(60)
        .build();

      // Act
      LoanApplicationResponseDTO result = loanService.submitLoanApplication(request);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getLoanType()).isEqualTo(loanType);
    }
  }

  @Test
  @DisplayName("Should handle minimum tenure (6 months)")
  void testSubmitLoanApplication_MinimumTenure() {
    // Arrange
    testRequestDTO.setTenureMonths(6);
    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    testLoan.setTenureMonths(6);
    when(loanRepository.save(any(Loan.class))).thenReturn(testLoan);

    // Act
    LoanApplicationResponseDTO result = loanService.submitLoanApplication(testRequestDTO);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getTenureMonths()).isEqualTo(6);
  }

  @Test
  @DisplayName("Should handle maximum tenure (360 months)")
  void testSubmitLoanApplication_MaximumTenure() {
    // Arrange
    testRequestDTO.setTenureMonths(360);
    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    testLoan.setTenureMonths(360);
    when(loanRepository.save(any(Loan.class))).thenReturn(testLoan);

    // Act
    LoanApplicationResponseDTO result = loanService.submitLoanApplication(testRequestDTO);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getTenureMonths()).isEqualTo(360);
  }

  @Test
  @DisplayName("Should handle large principal amount")
  void testSubmitLoanApplication_LargePrincipal() {
    // Arrange
    testRequestDTO.setPrincipalAmount(BigDecimal.valueOf(50000000)); // 5 crore
    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    testLoan.setPrincipalAmount(BigDecimal.valueOf(50000000));
    when(loanRepository.save(any(Loan.class))).thenReturn(testLoan);

    // Act
    LoanApplicationResponseDTO result = loanService.submitLoanApplication(testRequestDTO);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getPrincipalAmount()).isEqualTo(BigDecimal.valueOf(50000000));
  }
}
