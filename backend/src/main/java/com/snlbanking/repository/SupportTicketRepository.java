package com.snlbanking.repository;

import com.snlbanking.entity.SupportTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Support Ticket Repository
 * Data access layer for Support Ticket entity
 */
@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
  /**
   * Find support tickets by user ID
   */
  @Query("SELECT s FROM SupportTicket s WHERE s.user.userId = :userId")
Page<SupportTicket> findByUserId(@Param("userId") Long userId, Pageable pageable);

  /**
   * Find support ticket by ticket number
   */
  Optional<SupportTicket> findByTicketNumber(String ticketNumber);

  /**
   * Find support tickets by status
   */
  Page<SupportTicket> findByStatus(String status, Pageable pageable);

  /**
   * Find support tickets by user ID and status
   */
  @Query("SELECT s FROM SupportTicket s WHERE s.user.userId = :userId AND s.status = :status")
  Page<SupportTicket> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status, Pageable pageable);
}
