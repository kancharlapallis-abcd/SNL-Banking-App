package com.snlbanking.repository;

import com.snlbanking.entity.Loan;
import com.snlbanking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Loan Repository Interface
 * Provides database operations for Loan entity
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    /**
     * Find loan by loan number
     */
    Optional<Loan> findByLoanNumber(String loanNumber);

    /**
     * Find all loans for a user with pagination
     */
    Page<Loan> findByUser(User user, Pageable pageable);

    /**
     * Find active loans for a user
     */
    List<Loan> findByUserAndStatus(User user, Loan.LoanStatus status);

    /**
     * Find loans by type
     */
    Page<Loan> findByLoanType(Loan.LoanType loanType, Pageable pageable);

    /**
     * Find loans by status
     */
    Page<Loan> findByStatus(Loan.LoanStatus status, Pageable pageable);

    /**
     * Count active loans for a user
     */
    long countByUserAndStatus(User user, Loan.LoanStatus status);

    /**
     * Find loans pending approval
     */
    @Query("SELECT l FROM Loan l WHERE l.status = 'APPLIED' ORDER BY l.createdAt ASC")
    Page<Loan> findPendingLoans(Pageable pageable);

    /**
     * Find overdue loans
     */
    @Query("SELECT l FROM Loan l WHERE l.status = 'ACTIVE' AND l.remainingAmount > 0 " +
           "ORDER BY l.createdAt ASC")
    List<Loan> findActiveLoans();

    /**
     * Check if loan number exists
     */
    boolean existsByLoanNumber(String loanNumber);
}
