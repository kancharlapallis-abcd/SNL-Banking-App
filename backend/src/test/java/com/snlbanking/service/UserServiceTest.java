package com.snlbanking.service;

import com.snlbanking.dto.UserRegistrationDTO;
import com.snlbanking.dto.UserResponseDTO;
import com.snlbanking.entity.User;
import com.snlbanking.exception.DuplicateResourceException;
import com.snlbanking.exception.ResourceNotFoundException;
import com.snlbanking.repository.UserRepository;
import com.snlbanking.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for UserService
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRegistrationDTO registrationDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .mobileNumber("9876543210")
                .role(User.UserRole.CUSTOMER)
                .status(User.UserStatus.ACTIVE)
                .build();

        registrationDTO = UserRegistrationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .password("Password@123")
                .mobileNumber("9876543210")
                .panNumber("AAAAA0001A")
                .aadharNumber("123456789012")
                .build();
    }

    @Test
    @DisplayName("Register User - Success Case")
    void testRegisterUser_Success() {
        when(userRepository.existsByEmail(registrationDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.registerUser(registrationDTO);

        assertNotNull(response);
        assertEquals("john@example.com", response.getEmail());
        assertEquals("John", response.getFirstName());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).existsByEmail(registrationDTO.getEmail());
    }

    @Test
    @DisplayName("Register User - Duplicate Email")
    void testRegisterUser_DuplicateEmail() {
        when(userRepository.existsByEmail(registrationDTO.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> userService.registerUser(registrationDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Get User By ID - Success Case")
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals("john@example.com", response.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get User By ID - Not Found")
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get User By Email - Success Case")
    void testGetUserByEmail_Success() {
        when(userRepository.findByEmailIgnoreCase("john@example.com")).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByEmail("john@example.com");

        assertNotNull(foundUser);
        assertEquals("john@example.com", foundUser.getEmail());
        verify(userRepository, times(1)).findByEmail("john@example.com");
    }

    @Test
    @DisplayName("Get User By Email - Not Found")
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
                () -> userService.getUserByEmail("nonexistent@example.com"));
    }

    @Test
    @DisplayName("Change Password - Success Case")
    void testChangePassword_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> userService.changePassword(1L, "oldPassword", "newPassword"));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Change Password - Wrong Old Password")
    void testChangePassword_WrongOldPassword() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, 
                () -> userService.changePassword(1L, "wrongPassword", "newPassword"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Activate User - Success Case")
    void testActivateUser_Success() {
        user.setStatus(User.UserStatus.INACTIVE);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> userService.activateUser(1L));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deactivate User - Success Case")
    void testDeactivateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> userService.deactivateUser(1L));
        verify(userRepository, times(1)).save(any(User.class));
    }
}
