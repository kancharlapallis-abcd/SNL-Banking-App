package com.snlbanking.service.impl;

import com.snlbanking.dto.LoanApplicationRequestDTO;
import com.snlbanking.dto.LoanApplicationResponseDTO;
import com.snlbanking.entity.Loan;
import com.snlbanking.entity.User;
import com.snlbanking.exception.InvalidOperationException;
import com.snlbanking.exception.ResourceNotFoundException;
import com.snlbanking.repository.LoanRepository;
import com.snlbanking.repository.UserRepository;
import com.snlbanking.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Loan Service Implementation
 * Implements loan business logic
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    @Override
    public LoanApplicationResponseDTO submitLoanApplication(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Processing loan application for user ID: {}", loanApplicationRequestDTO.getUserId());

        // Validate input
        if (loanApplicationRequestDTO == null || loanApplicationRequestDTO.getPrincipalAmount() == null ||
                loanApplicationRequestDTO.getPrincipalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Loan application data is invalid");
        }

        // Get user
        User user = userRepository.findById(loanApplicationRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", 
                        loanApplicationRequestDTO.getUserId()));

        // Calculate EMI
        BigDecimal emiAmount = calculateEMI(
                loanApplicationRequestDTO.getPrincipalAmount(),
                loanApplicationRequestDTO.getInterestRate(),
                loanApplicationRequestDTO.getTenureMonths());

        // Create loan
        String loanNumber = generateLoanNumber();
        Loan loan = Loan.builder()
                .loanNumber(loanNumber)
                .user(user)
                .loanType(Loan.LoanType.valueOf(loanApplicationRequestDTO.getLoanType()))
                .principalAmount(loanApplicationRequestDTO.getPrincipalAmount())
                .interestRate(loanApplicationRequestDTO.getInterestRate())
                .tenureMonths(loanApplicationRequestDTO.getTenureMonths())
                .emiAmount(emiAmount)
                .remainingAmount(loanApplicationRequestDTO.getPrincipalAmount())
                .status(Loan.LoanStatus.PENDING)
                .build();

        // Save loan
        Loan savedLoan = loanRepository.save(loan);

        log.info("Loan application submitted successfully. Loan number: {}", loanNumber);

        return mapToResponseDTO(savedLoan);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanApplicationResponseDTO getLoanById(Long loanId) {
        log.debug("Fetching loan with ID: {}", loanId);

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", loanId));

        return mapToResponseDTO(loan);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanApplicationResponseDTO> getUserLoans(Long userId, Pageable pageable) {
        log.debug("Fetching loans for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return loanRepository.findByUser(user, pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanApplicationResponseDTO> getAllLoans(Pageable pageable) {
        log.debug("Fetching all loans");

        return loanRepository.findAll(pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    public void approveLoan(Long loanId) {
        log.info("Approving loan with ID: {}", loanId);

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", loanId));

        if (loan.getStatus() != Loan.LoanStatus.PENDING) {
            throw new InvalidOperationException("approveLoan", 
                    "Only pending loans can be approved");
        }

        loan.setStatus(Loan.LoanStatus.ACTIVE);
        loanRepository.save(loan);

        log.info("Loan approved successfully with ID: {}", loanId);
    }

    @Override
    public void rejectLoan(Long loanId) {
        log.info("Rejecting loan with ID: {}", loanId);

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", loanId));

        if (loan.getStatus() != Loan.LoanStatus.PENDING) {
            throw new InvalidOperationException("rejectLoan", 
                    "Only pending loans can be rejected");
        }

        loan.setStatus(Loan.LoanStatus.REJECTED);
        loanRepository.save(loan);

        log.info("Loan rejected successfully with ID: {}", loanId);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanApplicationResponseDTO getLoanByNumber(String loanNumber) {
        log.debug("Fetching loan with number: {}", loanNumber);

        if (loanNumber == null || loanNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Loan number cannot be null or empty");
        }

        Loan loan = loanRepository.findByLoanNumber(loanNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "number", loanNumber));

        return mapToResponseDTO(loan);
    }

    /**
     * Calculate EMI (Equated Monthly Installment)
     * Formula: EMI = [P * R * (1 + R)^N] / [(1 + R)^N - 1]
     * P = Principal, R = Monthly interest rate, N = Number of months
     */
    private BigDecimal calculateEMI(BigDecimal principal, Double annualRate, Integer months) {
        BigDecimal monthlyRate = BigDecimal.valueOf(annualRate / 12 / 100);
        BigDecimal monthsDecimal = BigDecimal.valueOf(months);
        
        // (1 + R)^N
        BigDecimal onePlusR = monthlyRate.add(BigDecimal.ONE);
        BigDecimal numerator = principal
                .multiply(monthlyRate)
                .multiply(onePlusR.pow(months));
        
        // (1 + R)^N - 1
        BigDecimal denominator = onePlusR.pow(months).subtract(BigDecimal.ONE);
        
        return numerator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Generate unique loan number
     */
    private String generateLoanNumber() {
        return "LOAN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Map Loan entity to ResponseDTO
     */
    private LoanApplicationResponseDTO mapToResponseDTO(Loan loan) {
        return LoanApplicationResponseDTO.builder()
                .loanApplicationId(loan.getLoanId())
                .loanNumber(loan.getLoanNumber())
                .userId(loan.getUser().getUserId())
                .userFirstName(loan.getUser().getFirstName())
                .userLastName(loan.getUser().getLastName())
                .loanType(loan.getLoanType().name())
                .principalAmount(loan.getPrincipalAmount())
                .interestRate(loan.getInterestRate())
                .tenureMonths(loan.getTenureMonths())
                .emiAmount(loan.getEmiAmount())
                .remainingAmount(loan.getRemainingAmount())
                .status(loan.getStatus().name())
                .createdAt(loan.getCreatedAt())
                .updatedAt(loan.getUpdatedAt())
                .build();
    }
}
