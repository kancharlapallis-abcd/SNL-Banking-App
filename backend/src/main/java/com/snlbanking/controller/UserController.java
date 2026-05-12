package com.snlbanking.controller;

import com.snlbanking.dto.APIResponseDTO;
import com.snlbanking.dto.UserRegistrationDTO;
import com.snlbanking.dto.UserResponseDTO;
import com.snlbanking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.snlbanking.dto.LoginRequestDTO;

/**
 * User REST Controller
 * Handles user authentication, registration, and profile management
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "User registration, authentication, and profile management APIs")
public class UserController {

    private final UserService userService;

    /**
     * POST /v1/users/register - Register new user
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account in the banking system")
    public ResponseEntity<APIResponseDTO<UserResponseDTO>> registerUser(
            @Valid @RequestBody UserRegistrationDTO registrationDTO) {
        log.info("User registration request received for email: {}", registrationDTO.getEmail());

        UserResponseDTO userResponse = userService.registerUser(registrationDTO);

        APIResponseDTO<UserResponseDTO> response = APIResponseDTO.<UserResponseDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("User registered successfully")
                .data(userResponse)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /v1/users/{id} - Get user by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve user profile information by user ID")
    public ResponseEntity<APIResponseDTO<UserResponseDTO>> getUserById(@PathVariable Long id) {
        log.info("Get user request received for ID: {}", id);

        UserResponseDTO userResponse = userService.getUserById(id);

        APIResponseDTO<UserResponseDTO> response = APIResponseDTO.<UserResponseDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User retrieved successfully")
                .data(userResponse)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/users - Get all users with pagination
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve all users with pagination support")
    public ResponseEntity<APIResponseDTO<Page<UserResponseDTO>>> getAllUsers(Pageable pageable) {
        log.info("Get all users request received");

        Page<UserResponseDTO> users = userService.getAllUsers(pageable);

        APIResponseDTO<Page<UserResponseDTO>> response = APIResponseDTO.<Page<UserResponseDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Users retrieved successfully")
                .data(users)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }
/**
     * POST /v1/users/login - User Authentication
     */
    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate user and return JWT token")
    public ResponseEntity<APIResponseDTO<UserResponseDTO>> login(@RequestBody LoginRequestDTO loginRequest) {
        log.info("Login request received for email: {}", loginRequest.getEmail());

        // Assuming your userService has a login method that validates credentials
        UserResponseDTO userResponse = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        APIResponseDTO<UserResponseDTO> response = APIResponseDTO.<UserResponseDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login successful")
                .data(userResponse)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }
    /**
     * PUT /v1/users/{id} - Update user profile
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update user profile", description = "Update user profile information")
    public ResponseEntity<APIResponseDTO<UserResponseDTO>> updateUserProfile(
            @PathVariable Long id,
            @Valid @RequestBody UserRegistrationDTO updateDTO) {
        log.info("Update user profile request received for ID: {}", id);

        UserResponseDTO userResponse = userService.updateUserProfile(id, updateDTO);

        APIResponseDTO<UserResponseDTO> response = APIResponseDTO.<UserResponseDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User profile updated successfully")
                .data(userResponse)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * POST /v1/users/{id}/change-password - Change password
     */
    @PostMapping("/{id}/change-password")
    @Operation(summary = "Change user password", description = "Change password for authenticated user")
    public ResponseEntity<APIResponseDTO<String>> changePassword(
            @PathVariable Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        log.info("Change password request received for ID: {}", id);

        userService.changePassword(id, oldPassword, newPassword);

        APIResponseDTO<String> response = APIResponseDTO.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password changed successfully")
                .data("Password updated")
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * POST /v1/users/{id}/activate - Activate user
     */
    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate user account", description = "Activate a suspended or inactive user account")
    public ResponseEntity<APIResponseDTO<String>> activateUser(@PathVariable Long id) {
        log.info("Activate user request received for ID: {}", id);

        userService.activateUser(id);

        APIResponseDTO<String> response = APIResponseDTO.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User activated successfully")
                .data("User is now active")
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * POST /v1/users/{id}/deactivate - Deactivate user
     */
    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate user account", description = "Deactivate a user account")
    public ResponseEntity<APIResponseDTO<String>> deactivateUser(@PathVariable Long id) {
        log.info("Deactivate user request received for ID: {}", id);

        userService.deactivateUser(id);

        APIResponseDTO<String> response = APIResponseDTO.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User deactivated successfully")
                .data("User is now inactive")
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /v1/users/{id} - Delete user
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Soft delete a user account")
    public ResponseEntity<APIResponseDTO<String>> deleteUser(@PathVariable Long id) {
        log.info("Delete user request received for ID: {}", id);

        userService.deleteUser(id);

        APIResponseDTO<String> response = APIResponseDTO.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User deleted successfully")
                .data("User account has been suspended")
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }
}
