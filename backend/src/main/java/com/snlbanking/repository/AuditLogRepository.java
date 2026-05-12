package com.snlbanking.repository;

import com.snlbanking.entity.AuditLog;
import com.snlbanking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Audit Log Repository Interface
 * Provides database operations for AuditLog entity
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    /**
     * Find audit logs for a user with pagination
     */
    Page<AuditLog> findByUser(User user, Pageable pageable);

    /**
     * Find audit logs by action
     */
    Page<AuditLog> findByAction(String action, Pageable pageable);

    /**
     * Find audit logs by status
     */
    Page<AuditLog> findByStatus(AuditLog.AuditStatus status, Pageable pageable);

    /**
     * Find audit logs within date range
     */
    @Query("SELECT a FROM AuditLog a WHERE a.createdAt BETWEEN :startDate AND :endDate ORDER BY a.createdAt DESC")
    Page<AuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate,
                                   Pageable pageable);

    /**
     * Find audit logs for user within date range
     */
    @Query("SELECT a FROM AuditLog a WHERE a.user = :user AND a.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY a.createdAt DESC")
    Page<AuditLog> findUserAuditsByDateRange(@Param("user") User user,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate,
                                             Pageable pageable);

    /**
     * Find failed audit logs
     */
    Page<AuditLog> findByUserAndStatus(User user, AuditLog.AuditStatus status, Pageable pageable);

    /**
     * Count failed login attempts for user
     */
    long countByUserAndActionAndStatus(User user, String action, AuditLog.AuditStatus status);
}
