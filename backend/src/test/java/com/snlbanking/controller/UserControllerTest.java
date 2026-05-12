package com.snlbanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snlbanking.dto.UserRegistrationDTO;
import com.snlbanking.dto.UserResponseDTO;
import com.snlbanking.entity.User;
import com.snlbanking.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit Tests for UserController
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@WebMvcTest(UserController.class)
@DisplayName("User Controller Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRegistrationDTO registrationDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        registrationDTO = UserRegistrationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .password("Password@123")
                .mobileNumber("9876543210")
                .panNumber("AAAAA0001A")
                .aadharNumber("123456789012")
                .build();

        userResponseDTO = UserResponseDTO.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .mobileNumber("9876543210")
                .role(User.UserRole.CUSTOMER)
                .status(User.UserStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Register User - Success Case")
    void testRegisterUser_Success() throws Exception {
        when(userService.registerUser(any(UserRegistrationDTO.class)))
                .thenReturn(userResponseDTO);

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    @DisplayName("Get User By ID - Success Case")
    void testGetUserById_Success() throws Exception {
        when(userService.getUserById(1L))
                .thenReturn(userResponseDTO);

        mockMvc.perform(get("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("User retrieved successfully"))
                .andExpect(jsonPath("$.data.userId").value(1));
    }

    @Test
    @DisplayName("Register User - Validation Error")
    void testRegisterUser_ValidationError() throws Exception {
        UserRegistrationDTO invalidDTO = UserRegistrationDTO.builder()
                .firstName("")
                .lastName("Doe")
                .email("invalid-email")
                .password("weak")
                .build();

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Activate User - Success Case")
    void testActivateUser_Success() throws Exception {
        mockMvc.perform(post("/api/v1/users/1/activate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("User activated successfully"));
    }

    @Test
    @DisplayName("Deactivate User - Success Case")
    void testDeactivateUser_Success() throws Exception {
        mockMvc.perform(post("/api/v1/users/1/deactivate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("User deactivated successfully"));
    }
}
