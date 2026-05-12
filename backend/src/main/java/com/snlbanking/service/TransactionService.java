package com.snlbanking.service;

import com.snlbanking.dto.FundTransferDTO;
import com.snlbanking.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction Service Interface
 * Defines business logic operations for Transaction entity
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
public interface TransactionService {

    /**
     * Transfer funds between accounts
     */
    Transaction transferFunds(FundTransferDTO transferDTO);

    /**
     * Get transaction by ID
     */
    Transaction getTransactionById(Long transactionId);

    /**
     * Get transaction by reference
     */
    Transaction getTransactionByReference(String transactionReference);

    /**
     * Get all transactions for an account
     */
    Page<Transaction> getAccountTransactions(Long accountId, Pageable pageable);

    /**
     * Get outgoing transactions for an account
     */
    Page<Transaction> getOutgoingTransactions(Long accountId, Pageable pageable);

    /**
     * Get incoming transactions for an account
     */
    Page<Transaction> getIncomingTransactions(Long accountId, Pageable pageable);

    /**
     * Get transactions within date range
     */
    Page<Transaction> getTransactionsByDateRange(Long accountId, LocalDateTime startDate, 
                                                 LocalDateTime endDate, Pageable pageable);

    /**
     * Get transactions by type
     */
    Page<Transaction> getTransactionsByType(Transaction.TransactionType type, Pageable pageable);

    /**
     * Get transactions by status
     */
    Page<Transaction> getTransactionsByStatus(Transaction.TransactionStatus status, Pageable pageable);

    /**
     * Calculate total outgoing amount for an account
     */
    BigDecimal calculateTotalOutgoing(Long accountId);

    /**
     * Reverse a transaction
     */
    void reverseTransaction(Long transactionId);

    /**
     * Get all transactions with pagination
     */
    Page<Transaction> getAllTransactions(Pageable pageable);
}
