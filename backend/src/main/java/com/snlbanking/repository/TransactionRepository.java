package com.snlbanking.repository;

import com.snlbanking.entity.Account;
import com.snlbanking.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Transaction Repository Interface
 * Provides database operations for Transaction entity
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find transaction by reference
     */
    Optional<Transaction> findByTransactionReference(String transactionReference);

    /**
     * Find all transactions for an account (both sent and received)
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount = :account OR t.toAccount = :account) " +
           "ORDER BY t.createdAt DESC")
    Page<Transaction> findTransactionsByAccount(@Param("account") Account account, Pageable pageable);

    /**
     * Find outgoing transactions for an account
     */
    Page<Transaction> findByFromAccount(Account account, Pageable pageable);

    /**
     * Find incoming transactions for an account
     */
    Page<Transaction> findByToAccount(Account account, Pageable pageable);

    /**
     * Find transactions by type
     */
    Page<Transaction> findByTransactionType(Transaction.TransactionType type, Pageable pageable);

    /**
     * Find transactions by status
     */
    Page<Transaction> findByStatus(Transaction.TransactionStatus status, Pageable pageable);

    /**
     * Find transactions within date range for an account
     */
    @Query("SELECT t FROM Transaction t WHERE t.fromAccount = :account AND t.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY t.createdAt DESC")
    Page<Transaction> findTransactionsByDateRange(@Param("account") Account account,
                                                  @Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate,
                                                  Pageable pageable);

    /**
     * Calculate total transaction amount for an account
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.fromAccount = :account AND t.status = 'COMPLETED'")
    BigDecimal calculateTotalOutgoing(@Param("account") Account account);

    /**
     * Count transactions by status for an account
     */
    long countByFromAccountAndStatus(Account account, Transaction.TransactionStatus status);
}
