package com.snlbanking.service.impl;

import com.snlbanking.dto.FundTransferDTO;
import com.snlbanking.entity.Account;
import com.snlbanking.entity.Transaction;
import com.snlbanking.exception.InsufficientBalanceException;
import com.snlbanking.exception.InvalidOperationException;
import com.snlbanking.exception.ResourceNotFoundException;
import com.snlbanking.repository.AccountRepository;
import com.snlbanking.repository.TransactionRepository;
import com.snlbanking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Transaction Service Implementation
 * Implements business logic for Transaction entity
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    private static final BigDecimal TRANSACTION_CHARGE = new BigDecimal("10.00");

    @Override
    public Transaction transferFunds(FundTransferDTO transferDTO) {
        log.info("Processing fund transfer from account ID: {} to account: {}",
                transferDTO.getFromAccountId(), transferDTO.getToAccountNumber());

        // Validate input
        if (transferDTO == null || transferDTO.getAmount() == null || 
            transferDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Fund transfer data is invalid");
        }

        // Get from account
        Account fromAccount = accountRepository.findById(transferDTO.getFromAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", transferDTO.getFromAccountId()));

        // Get to account
        Account toAccount = accountRepository.findByAccountNumber(transferDTO.getToAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "accountNumber", 
                        transferDTO.getToAccountNumber()));

        // Validate accounts
        validateTransferEligibility(fromAccount, toAccount);

        // Check balance
        BigDecimal totalAmount = transferDTO.getAmount().add(TRANSACTION_CHARGE);
        if (fromAccount.getBalance().compareTo(totalAmount) < 0) {
            log.warn("Insufficient balance in account: {}", fromAccount.getAccountNumber());
            throw new InsufficientBalanceException(fromAccount.getAccountNumber(),
                    fromAccount.getBalance().doubleValue(), totalAmount.doubleValue());
        }

        // Create transaction
        String transactionRef = generateTransactionReference();
        Transaction transaction = Transaction.builder()
                .transactionReference(transactionRef)
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(transferDTO.getAmount())
                .transactionType(Transaction.TransactionType.INTRA_BANK_TRANSFER)
                .status(Transaction.TransactionStatus.PENDING)
                .description(transferDTO.getDescription())
                .referenceNumber(transferDTO.getReferenceNumber())
                .charges(TRANSACTION_CHARGE)
                .build();

        // Update account balances
        fromAccount.setBalance(fromAccount.getBalance().subtract(totalAmount));
        toAccount.setBalance(toAccount.getBalance().add(transferDTO.getAmount()));

        // Save transaction and update accounts
        Transaction savedTransaction = transactionRepository.save(transaction);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Mark transaction as completed
        savedTransaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        Transaction completedTransaction = transactionRepository.save(savedTransaction);

        log.info("Fund transfer completed successfully. Transaction reference: {}", transactionRef);

        return completedTransaction;
    }

    @Override
    @Transactional(readOnly = true)
    public Transaction getTransactionById(Long transactionId) {
        log.debug("Fetching transaction with ID: {}", transactionId);

        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionId));
    }

    @Override
    @Transactional(readOnly = true)
    public Transaction getTransactionByReference(String transactionReference) {
        log.debug("Fetching transaction with reference: {}", transactionReference);

        if (transactionReference == null || transactionReference.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction reference cannot be null or empty");
        }

        return transactionRepository.findByTransactionReference(transactionReference)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "reference", transactionReference));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> getAccountTransactions(Long accountId, Pageable pageable) {
        log.debug("Fetching transactions for account ID: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));

        return transactionRepository.findTransactionsByAccount(account, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> getOutgoingTransactions(Long accountId, Pageable pageable) {
        log.debug("Fetching outgoing transactions for account ID: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));

        return transactionRepository.findByFromAccount(account, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> getIncomingTransactions(Long accountId, Pageable pageable) {
        log.debug("Fetching incoming transactions for account ID: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));

        return transactionRepository.findByToAccount(account, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> getTransactionsByDateRange(Long accountId, LocalDateTime startDate,
                                                       LocalDateTime endDate, Pageable pageable) {
        log.debug("Fetching transactions for account ID: {} between {} and {}", accountId, startDate, endDate);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));

        return transactionRepository.findTransactionsByDateRange(account, startDate, endDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> getTransactionsByType(Transaction.TransactionType type, Pageable pageable) {
        log.debug("Fetching transactions by type: {}", type);

        return transactionRepository.findByTransactionType(type, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> getTransactionsByStatus(Transaction.TransactionStatus status, Pageable pageable) {
        log.debug("Fetching transactions by status: {}", status);

        return transactionRepository.findByStatus(status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalOutgoing(Long accountId) {
        log.debug("Calculating total outgoing for account ID: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));

        return transactionRepository.calculateTotalOutgoing(account);
    }

    @Override
    public void reverseTransaction(Long transactionId) {
        log.info("Reversing transaction ID: {}", transactionId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionId));

        if (!transaction.getStatus().equals(Transaction.TransactionStatus.COMPLETED)) {
            log.warn("Cannot reverse transaction with status: {}", transaction.getStatus());
            throw new InvalidOperationException("reverseTransaction", 
                    "Only completed transactions can be reversed");
        }

        // Reverse the transaction
        transaction.setStatus(Transaction.TransactionStatus.REVERSED);

        // Reverse account balances
        Account fromAccount = transaction.getFromAccount();
        Account toAccount = transaction.getToAccount();

        fromAccount.setBalance(fromAccount.getBalance().add(transaction.getAmount()).add(transaction.getCharges()));
        toAccount.setBalance(toAccount.getBalance().subtract(transaction.getAmount()));

        transactionRepository.save(transaction);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        log.info("Transaction reversed successfully with ID: {}", transactionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> getAllTransactions(Pageable pageable) {
        log.debug("Fetching all transactions with pagination");

        return transactionRepository.findAll(pageable);
    }

    /**
     * Generate unique transaction reference
     */
    private String generateTransactionReference() {
        return "TXN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Validate transfer eligibility
     */
    private void validateTransferEligibility(Account fromAccount, Account toAccount) {
        if (fromAccount.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new InvalidOperationException("transferFunds", "From account is not active");
        }

        if (toAccount.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new InvalidOperationException("transferFunds", "To account is not active");
        }

        if (fromAccount.getAccountId().equals(toAccount.getAccountId())) {
            throw new InvalidOperationException("transferFunds", "Cannot transfer to the same account");
        }
    }
}
