package com.snlbanking.service;

import com.snlbanking.dto.SupportTicketRequestDTO;
import com.snlbanking.dto.SupportTicketResponseDTO;
import com.snlbanking.entity.SupportTicket;
import com.snlbanking.entity.User;
import com.snlbanking.exception.ResourceNotFoundException;
import com.snlbanking.repository.SupportTicketRepository;
import com.snlbanking.repository.UserRepository;
import com.snlbanking.service.impl.SupportTicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SupportTicketService Unit Tests")
class SupportTicketServiceTest {
  @Mock
  private SupportTicketRepository supportTicketRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private SupportTicketServiceImpl supportTicketService;

  private User testUser;
  private SupportTicket testTicket;
  private SupportTicketRequestDTO testRequestDTO;

  @BeforeEach
  void setUp() {
    testUser = User.builder()
      .userId(1L)
      .firstName("John")
      .lastName("Doe")
      .email("john@example.com")
      .build();

    testTicket = SupportTicket.builder()
      .ticketId(1L)
      .ticketNumber("TICKET123")
      .user(testUser)
      .category("TECHNICAL_ISSUE")
      .subject("Login not working")
      .description("Unable to login to my account")
      .status("OPEN")
      .createdAt(LocalDateTime.now())
      .updatedAt(LocalDateTime.now())
      .build();

    testRequestDTO = SupportTicketRequestDTO.builder()
      .userId(1L)
      .category("TECHNICAL_ISSUE")
      .subject("Login not working")
      .description("Unable to login to my account")
      .build();
  }

  @Test
  @DisplayName("Should successfully submit support ticket with OPEN status")
  void testSubmitTicket_Success() {
    // Arrange
    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    when(supportTicketRepository.save(any(SupportTicket.class))).thenReturn(testTicket);

    // Act
    SupportTicketResponseDTO result = supportTicketService.submitTicket(testRequestDTO);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getTicketId()).isEqualTo(1L);
    assertThat(result.getStatus()).isEqualTo("OPEN");
    assertThat(result.getCategory()).isEqualTo("TECHNICAL_ISSUE");
    assertThat(result.getUserFirstName()).isEqualTo("John");

    verify(userRepository, times(1)).findById(1L);
    verify(supportTicketRepository, times(1)).save(any(SupportTicket.class));
  }

  @Test
  @DisplayName("Should throw ResourceNotFoundException when user not found")
  void testSubmitTicket_UserNotFound() {
    // Arrange
    when(userRepository.findById(999L)).thenReturn(Optional.empty());

    SupportTicketRequestDTO invalidRequest = SupportTicketRequestDTO.builder()
      .userId(999L)
      .category("TECHNICAL_ISSUE")
      .subject("Login not working")
      .description("Unable to login to my account")
      .build();

    // Act & Assert
    assertThatThrownBy(() -> supportTicketService.submitTicket(invalidRequest))
      .isInstanceOf(ResourceNotFoundException.class)
      .hasMessageContaining("User not found");

    verify(supportTicketRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should get ticket by ID successfully")
  void testGetTicketById_Success() {
    // Arrange
    when(supportTicketRepository.findById(1L)).thenReturn(Optional.of(testTicket));

    // Act
    SupportTicketResponseDTO result = supportTicketService.getTicketById(1L);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getTicketId()).isEqualTo(1L);
    assertThat(result.getTicketNumber()).isEqualTo("TICKET123");
    verify(supportTicketRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Should throw ResourceNotFoundException when ticket not found")
  void testGetTicketById_NotFound() {
    // Arrange
    when(supportTicketRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> supportTicketService.getTicketById(999L))
      .isInstanceOf(ResourceNotFoundException.class)
      .hasMessageContaining("Support ticket not found");
  }

  @Test
  @DisplayName("Should retrieve all tickets for a user with pagination")
  void testGetUserTickets_Success() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    Page<SupportTicket> ticketPage = new PageImpl<>(Arrays.asList(testTicket), pageable, 1);
    when(userRepository.existsById(1L)).thenReturn(true);
    when(supportTicketRepository.findByUserId(1L, pageable)).thenReturn(ticketPage);

    // Act
    Page<SupportTicketResponseDTO> result = supportTicketService.getUserTickets(1L, pageable);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getTotalElements()).isEqualTo(1);
    verify(supportTicketRepository, times(1)).findByUserId(1L, pageable);
  }

  @Test
  @DisplayName("Should throw exception when getting tickets for non-existent user")
  void testGetUserTickets_UserNotFound() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    when(userRepository.existsById(999L)).thenReturn(false);

    // Act & Assert
    assertThatThrownBy(() -> supportTicketService.getUserTickets(999L, pageable))
      .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should retrieve all tickets with pagination")
  void testGetAllTickets_Success() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    Page<SupportTicket> ticketPage = new PageImpl<>(Arrays.asList(testTicket), pageable, 1);
    when(supportTicketRepository.findAll(pageable)).thenReturn(ticketPage);

    // Act
    Page<SupportTicketResponseDTO> result = supportTicketService.getAllTickets(pageable);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getContent()).hasSize(1);
    verify(supportTicketRepository, times(1)).findAll(pageable);
  }

  @Test
  @DisplayName("Should successfully close ticket and change status to CLOSED")
  void testCloseTicket_Success() {
    // Arrange
    testTicket.setStatus("OPEN");
    when(supportTicketRepository.findById(1L)).thenReturn(Optional.of(testTicket));
    when(supportTicketRepository.save(any(SupportTicket.class))).thenReturn(testTicket);

    // Act
    SupportTicketResponseDTO result = supportTicketService.closeTicket(1L);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getStatus()).isEqualTo("CLOSED");
    verify(supportTicketRepository, times(1)).save(any(SupportTicket.class));
  }

  @Test
  @DisplayName("Should throw exception when closing non-existent ticket")
  void testCloseTicket_NotFound() {
    // Arrange
    when(supportTicketRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> supportTicketService.closeTicket(999L))
      .isInstanceOf(ResourceNotFoundException.class);

    verify(supportTicketRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should get ticket by ticket number successfully")
  void testGetTicketByNumber_Success() {
    // Arrange
    when(supportTicketRepository.findByTicketNumber("TICKET123")).thenReturn(Optional.of(testTicket));

    // Act
    SupportTicketResponseDTO result = supportTicketService.getTicketByNumber("TICKET123");

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getTicketNumber()).isEqualTo("TICKET123");
    verify(supportTicketRepository, times(1)).findByTicketNumber("TICKET123");
  }

  @Test
  @DisplayName("Should throw exception for non-existent ticket number")
  void testGetTicketByNumber_NotFound() {
    // Arrange
    when(supportTicketRepository.findByTicketNumber("INVALID")).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> supportTicketService.getTicketByNumber("INVALID"))
      .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should support all ticket categories")
  void testSubmitTicket_DifferentCategories() {
    // Arrange
    String[] categories = {"GENERAL", "TECHNICAL_ISSUE", "ACCOUNT_PROBLEM", "TRANSACTION_ISSUE", "COMPLAINT"};

    for (String category : categories) {
      SupportTicket ticket = SupportTicket.builder()
        .ticketId(1L)
        .ticketNumber("TICKET123")
        .user(testUser)
        .category(category)
        .subject("Test subject")
        .description("Test description")
        .status("OPEN")
        .build();

      when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
      when(supportTicketRepository.save(any(SupportTicket.class))).thenReturn(ticket);

      SupportTicketRequestDTO request = SupportTicketRequestDTO.builder()
        .userId(1L)
        .category(category)
        .subject("Test subject")
        .description("Test description")
        .build();

      // Act
      SupportTicketResponseDTO result = supportTicketService.submitTicket(request);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getCategory()).isEqualTo(category);
    }
  }

  @Test
  @DisplayName("Should preserve long description text")
  void testSubmitTicket_LongDescription() {
    // Arrange
    String longDescription = "This is a very long description ".repeat(50);
    testRequestDTO.setDescription(longDescription);
    testTicket.setDescription(longDescription);

    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    when(supportTicketRepository.save(any(SupportTicket.class))).thenReturn(testTicket);

    // Act
    SupportTicketResponseDTO result = supportTicketService.submitTicket(testRequestDTO);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getDescription()).isEqualTo(longDescription);
  }

  @Test
  @DisplayName("Should handle pagination with multiple tickets")
  void testGetUserTickets_MultiplePages() {
    // Arrange
    SupportTicket ticket2 = SupportTicket.builder()
      .ticketId(2L)
      .ticketNumber("TICKET124")
      .user(testUser)
      .category("ACCOUNT_PROBLEM")
      .subject("Account frozen")
      .description("My account is frozen")
      .status("IN_PROGRESS")
      .build();

    Pageable pageable = PageRequest.of(0, 10);
    Page<SupportTicket> ticketPage = new PageImpl<>(Arrays.asList(testTicket, ticket2), pageable, 2);

    when(userRepository.existsById(1L)).thenReturn(true);
    when(supportTicketRepository.findByUserId(1L, pageable)).thenReturn(ticketPage);

    // Act
    Page<SupportTicketResponseDTO> result = supportTicketService.getUserTickets(1L, pageable);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getContent()).hasSize(2);
    assertThat(result.getTotalElements()).isEqualTo(2);
  }
}
