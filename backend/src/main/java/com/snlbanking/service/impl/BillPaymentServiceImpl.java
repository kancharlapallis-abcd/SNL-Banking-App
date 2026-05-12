package com.snlbanking.service.impl;

import com.snlbanking.dto.BillPaymentRequestDTO;
import com.snlbanking.dto.BillPaymentResponseDTO;
import com.snlbanking.entity.Account;
import com.snlbanking.entity.BillPayment;
import com.snlbanking.exception.InsufficientBalanceException;
import com.snlbanking.exception.InvalidOperationException;
import com.snlbanking.exception.ResourceNotFoundException;
import com.snlbanking.repository.AccountRepository;
import com.snlbanking.repository.BillPaymentRepository;
import com.snlbanking.service.BillPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Bill Payment Service Implementation
 * Implements bill payment business logic
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BillPaymentServiceImpl implements BillPaymentService {

    private final BillPaymentRepository billPaymentRepository;
    private final AccountRepository accountRepository;

    @Override
    public BillPaymentResponseDTO payBill(BillPaymentRequestDTO billPaymentRequestDTO) {
        log.info("Processing bill payment for account ID: {}", billPaymentRequestDTO.getAccountId());

        // Validate input
        if (billPaymentRequestDTO == null || billPaymentRequestDTO.getBillAmount() == null ||
                billPaymentRequestDTO.getBillAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Bill payment data is invalid");
        }

        // Get account
        Account account = accountRepository.findById(billPaymentRequestDTO.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", 
                        billPaymentRequestDTO.getAccountId()));

        // Validate account is active
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new InvalidOperationException("payBill", "Account is not active");
        }

        // Check balance
        if (account.getBalance().compareTo(billPaymentRequestDTO.getBillAmount()) < 0) {
            log.warn("Insufficient balance in account: {}", account.getAccountNumber());
            throw new InsufficientBalanceException(account.getAccountNumber(),
                    account.getBalance().doubleValue(), 
                    billPaymentRequestDTO.getBillAmount().doubleValue());
        }

        // Create bill payment
        String billReference = generateBillReference();
        BillPayment billPayment = BillPayment.builder()
                .billReference(billReference)
                .account(account)
                .billType(BillPayment.BillType.valueOf(billPaymentRequestDTO.getBillType()))
                .billerName(billPaymentRequestDTO.getBillerName())
                .billAmount(billPaymentRequestDTO.getBillAmount())
                .billerReferenceNumber(billPaymentRequestDTO.getBillerReferenceNumber())
                .status(BillPayment.BillPaymentStatus.PENDING)
                .build();

        // Deduct amount from account
        account.setBalance(account.getBalance().subtract(billPaymentRequestDTO.getBillAmount()));

        // Save bill payment and update account
        BillPayment savedBillPayment = billPaymentRepository.save(billPayment);
        accountRepository.save(account);

        // Mark as completed
        savedBillPayment.setStatus(BillPayment.BillPaymentStatus.COMPLETED);
        BillPayment completedBillPayment = billPaymentRepository.save(savedBillPayment);

        log.info("Bill payment completed successfully. Bill reference: {}", billReference);

        return mapToResponseDTO(completedBillPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public BillPaymentResponseDTO getBillPaymentById(Long billPaymentId) {
        log.debug("Fetching bill payment with ID: {}", billPaymentId);

        BillPayment billPayment = billPaymentRepository.findById(billPaymentId)
                .orElseThrow(() -> new ResourceNotFoundException("BillPayment", "id", billPaymentId));

        return mapToResponseDTO(billPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BillPaymentResponseDTO> getAccountBillPayments(Long accountId, Pageable pageable) {
        log.debug("Fetching bill payments for account ID: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));

        return billPaymentRepository.findByAccount(account, pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BillPaymentResponseDTO> getAllBillPayments(Pageable pageable) {
        log.debug("Fetching all bill payments");

        return billPaymentRepository.findAll(pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    public void cancelBillPayment(Long billPaymentId) {
        log.info("Cancelling bill payment with ID: {}", billPaymentId);

        BillPayment billPayment = billPaymentRepository.findById(billPaymentId)
                .orElseThrow(() -> new ResourceNotFoundException("BillPayment", "id", billPaymentId));

        if (billPayment.getStatus() != BillPayment.BillPaymentStatus.PENDING) {
            throw new InvalidOperationException("cancelBillPayment", 
                    "Only pending bill payments can be cancelled");
        }

        // Refund amount back to account
        Account account = billPayment.getAccount();
        account.setBalance(account.getBalance().add(billPayment.getBillAmount()));

        billPayment.setStatus(BillPayment.BillPaymentStatus.CANCELLED);
        billPaymentRepository.save(billPayment);
        accountRepository.save(account);

        log.info("Bill payment cancelled successfully with ID: {}", billPaymentId);
    }

    @Override
    @Transactional(readOnly = true)
    public BillPaymentResponseDTO getBillPaymentByReference(String billReference) {
        log.debug("Fetching bill payment with reference: {}", billReference);

        if (billReference == null || billReference.trim().isEmpty()) {
            throw new IllegalArgumentException("Bill reference cannot be null or empty");
        }

        BillPayment billPayment = billPaymentRepository.findByBillReference(billReference)
                .orElseThrow(() -> new ResourceNotFoundException("BillPayment", "reference", billReference));

        return mapToResponseDTO(billPayment);
    }

    /**
     * Generate unique bill reference
     */
    private String generateBillReference() {
        return "BILL" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Map BillPayment entity to ResponseDTO
     */
    private BillPaymentResponseDTO mapToResponseDTO(BillPayment billPayment) {
        return BillPaymentResponseDTO.builder()
                .billPaymentId(billPayment.getBillPaymentId())
                .billReference(billPayment.getBillReference())
                .accountId(billPayment.getAccount().getAccountId())
                .accountNumber(billPayment.getAccount().getAccountNumber())
                .billType(billPayment.getBillType().name())
                .billerName(billPayment.getBillerName())
                .billAmount(billPayment.getBillAmount())
                .billerReferenceNumber(billPayment.getBillerReferenceNumber())
                .status(billPayment.getStatus().name())
                .createdAt(billPayment.getCreatedAt())
                .updatedAt(billPayment.getUpdatedAt())
                .build();
    }
}
