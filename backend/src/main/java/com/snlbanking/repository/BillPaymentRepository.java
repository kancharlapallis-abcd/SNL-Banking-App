package com.snlbanking.repository;

import com.snlbanking.entity.BillPayment;
import com.snlbanking.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Bill Payment Repository Interface
 * Provides database operations for BillPayment entity
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Repository
public interface BillPaymentRepository extends JpaRepository<BillPayment, Long> {

    /**
     * Find bill payment by reference
     */
    Optional<BillPayment> findByBillReference(String billReference);

    /**
     * Find all bill payments for an account with pagination
     */
    Page<BillPayment> findByAccount(Account account, Pageable pageable);

    /**
     * Find bill payments by account and status
     */
    Page<BillPayment> findByAccountAndStatus(Account account, BillPayment.BillPaymentStatus status, Pageable pageable);

    /**
     * Find bill payments by type
     */
    Page<BillPayment> findByBillType(BillPayment.BillType billType, Pageable pageable);

    /**
     * Find bill payments by status
     */
    Page<BillPayment> findByStatus(BillPayment.BillPaymentStatus status, Pageable pageable);

    /**
     * Find bill payments within date range
     */
    @Query("SELECT b FROM BillPayment b WHERE b.account = :account AND b.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY b.createdAt DESC")
    Page<BillPayment> findBillPaymentsByDateRange(@Param("account") Account account,
                                                  @Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate,
                                                  Pageable pageable);

    /**
     * Check if bill reference exists
     */
    boolean existsByBillReference(String billReference);

    /**
     * Count pending bills for an account
     */
    long countByAccountAndStatus(Account account, BillPayment.BillPaymentStatus status);
}
