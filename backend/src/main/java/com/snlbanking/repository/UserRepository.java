package com.snlbanking.repository;

import com.snlbanking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository Interface
 * Provides database operations for User entity
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     */
     Optional<User> findByEmail(String email);
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Find user by mobile number
     */
    Optional<User> findByMobileNumber(String mobileNumber);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find all active users with pagination
     */
    Page<User> findByStatus(User.UserStatus status, Pageable pageable);

    /**
     * Find users by role
     */
    Page<User> findByRole(User.UserRole role, Pageable pageable);

    /**
     * Find users by city
     */
    Page<User> findByCity(String city, Pageable pageable);

    /**
     * Search users by name
     */
    @Query("SELECT u FROM User u WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<User> searchByName(@Param("name") String name, Pageable pageable);
}
