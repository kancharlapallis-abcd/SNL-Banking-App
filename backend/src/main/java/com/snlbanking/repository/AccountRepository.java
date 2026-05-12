package com.snlbanking.repository;

import com.snlbanking.entity.Account;
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
 * Account Repository Interface
 * Provides database operations for Account entity
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Find account by account number
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Find all accounts by user with pagination
     */
    Page<Account> findByUser(User user, Pageable pageable);

    /**
     * Find all active accounts for a user
     */
    List<Account> findByUserAndStatus(User user, Account.AccountStatus status);

    /**
     * Find accounts by type
     */
    Page<Account> findByAccountType(Account.AccountType accountType, Pageable pageable);

    /**
     * Find accounts by status with pagination
     */
    Page<Account> findByStatus(Account.AccountStatus status, Pageable pageable);

    /**
     * Check if account number exists
     */
    boolean existsByAccountNumber(String accountNumber);

    /**
     * Count accounts by user
     */
    long countByUser(User user);

    /**
     * Find accounts by IFSC code
     */
    @Query("SELECT a FROM Account a WHERE a.ifscCode = :ifscCode AND a.status = :status")
    List<Account> findByIfscCodeAndStatus(@Param("ifscCode") String ifscCode, 
                                          @Param("status") Account.AccountStatus status);
}
