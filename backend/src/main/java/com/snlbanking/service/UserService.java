package com.snlbanking.service;

import com.snlbanking.dto.UserRegistrationDTO;
import com.snlbanking.dto.UserResponseDTO;
import com.snlbanking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * User Service Interface
 * Defines business logic operations for User entity
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
public interface UserService {

    /**
     * Register a new user
     */
    UserResponseDTO registerUser(UserRegistrationDTO registrationDTO);

    /**
     * Get user by ID
     */
    UserResponseDTO getUserById(Long userId);

    /**
     * Get user by email
     */
    User getUserByEmail(String email);

    /**
     * Update user profile
     */
    UserResponseDTO updateUserProfile(Long userId, UserRegistrationDTO updateDTO);

    /**
     * Get all users with pagination
     */
    Page<UserResponseDTO> getAllUsers(Pageable pageable);

    /**
     * Search users by name
     */
    Page<UserResponseDTO> searchUsersByName(String name, Pageable pageable);

    /**
     * Get users by role
     */
    Page<UserResponseDTO> getUsersByRole(User.UserRole role, Pageable pageable);

    /**
     * Get users by status
     */
    Page<UserResponseDTO> getUsersByStatus(User.UserStatus status, Pageable pageable);

    /**
     * Change user password
     */
    void changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * Reset user password
     */
    void resetPassword(String email);

    /**
     * Activate user account
     */
    void activateUser(Long userId);

    /**
     * Deactivate user account
     */
    void deactivateUser(Long userId);

    /**
     * Delete user (soft delete)
     */
    void deleteUser(Long userId);
    // Add this method to your existing UserService interface
UserResponseDTO login(String email, String password);
}
