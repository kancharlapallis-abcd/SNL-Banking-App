package com.snlbanking.service.impl;

import com.snlbanking.dto.UserRegistrationDTO;
import com.snlbanking.dto.UserResponseDTO;
import com.snlbanking.entity.User;
import com.snlbanking.exception.DuplicateResourceException;
import com.snlbanking.exception.ResourceNotFoundException;
import com.snlbanking.repository.UserRepository;
import com.snlbanking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.modelmapper.ModelMapper;

/**
 * User Service Implementation
 * Implements business logic for User entity
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

   @Override
    @Transactional
    public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        log.info("Registering new user with email: {}", registrationDTO.getEmail());

        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new com.snlbanking.exception.DuplicateResourceException("User", "email", registrationDTO.getEmail());
        }

        // Manually setting timestamps to satisfy Database NOT NULL constraints
        User user = User.builder()
                .firstName(registrationDTO.getFirstName())
                .lastName(registrationDTO.getLastName())
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .mobileNumber(registrationDTO.getMobileNumber())
                .panNumber(registrationDTO.getPanNumber())
                .aadharNumber(registrationDTO.getAadharNumber())
                .address(registrationDTO.getAddress())
                .city(registrationDTO.getCity())
                .state(registrationDTO.getState())
                .pincode(registrationDTO.getPincode())
                .role(User.UserRole.CUSTOMER)
                .status(User.UserStatus.VERIFIED_PENDING)
                .createdAt(java.time.LocalDateTime.now()) // CRITICAL FIX
                .updatedAt(java.time.LocalDateTime.now()) // CRITICAL FIX
                .build();

        User savedUser = userRepository.save(user);
        log.info("User saved to DB with ID: {}", savedUser.getUserId());
        return UserResponseDTO.fromEntity(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long userId) {
        log.debug("Fetching user with ID: {}", userId);

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be valid");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return UserResponseDTO.fromEntity(user);
    }

@Override
    public UserResponseDTO login(String email, String password) {
        log.info("Login attempt for: {}", email);
        
        String cleanEmail = email.trim().toLowerCase();
        User user = userRepository.findByEmailIgnoreCase(cleanEmail)
                .orElseThrow(() -> new com.snlbanking.exception.ResourceNotFoundException("User not found with email: " + email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid password");
        }

        UserResponseDTO response = UserResponseDTO.fromEntity(user);
        response.setToken("dummy-jwt-token-" + user.getUserId()); 
        
        return response;
    }
    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        log.debug("Fetching user with email: {}", email);

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Override
    public UserResponseDTO updateUserProfile(Long userId, UserRegistrationDTO updateDTO) {
        log.info("Updating user profile for ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setFirstName(updateDTO.getFirstName());
        user.setLastName(updateDTO.getLastName());
        user.setMobileNumber(updateDTO.getMobileNumber());
        user.setAddress(updateDTO.getAddress());
        user.setCity(updateDTO.getCity());
        user.setState(updateDTO.getState());
        user.setPincode(updateDTO.getPincode());

        User updatedUser = userRepository.save(user);
        log.info("User profile updated successfully for ID: {}", userId);

        return UserResponseDTO.fromEntity(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users with pagination");

        return userRepository.findAll(pageable)
                .map(UserResponseDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> searchUsersByName(String name, Pageable pageable) {
        log.debug("Searching users by name: {}", name);

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Search name cannot be null or empty");
        }

        return userRepository.searchByName(name, pageable)
                .map(UserResponseDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getUsersByRole(User.UserRole role, Pageable pageable) {
        log.debug("Fetching users by role: {}", role);

        return userRepository.findByRole(role, pageable)
                .map(UserResponseDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getUsersByStatus(User.UserStatus status, Pageable pageable) {
        log.debug("Fetching users by status: {}", status);

        return userRepository.findByStatus(status, pageable)
                .map(UserResponseDTO::fromEntity);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        log.info("Changing password for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.warn("Old password mismatch for user ID: {}", userId);
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password changed successfully for user ID: {}", userId);
    }

    @Override
    public void resetPassword(String email) {
        log.info("Resetting password for email: {}", email);

        User user = getUserByEmail(email);
        // In real implementation, send email with reset link
        // For now, just log the action
        log.info("Password reset initiated for user: {}", email);
    }

    @Override
    public void activateUser(Long userId) {
        log.info("Activating user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setStatus(User.UserStatus.ACTIVE);
        userRepository.save(user);
        log.info("User activated successfully with ID: {}", userId);
    }

    @Override
    public void deactivateUser(Long userId) {
        log.info("Deactivating user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setStatus(User.UserStatus.INACTIVE);
        userRepository.save(user);
        log.info("User deactivated successfully with ID: {}", userId);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Soft deleting user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setStatus(User.UserStatus.SUSPENDED);
        userRepository.save(user);
        log.info("User soft deleted successfully with ID: {}", userId);
    }
}
