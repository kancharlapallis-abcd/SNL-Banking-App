package com.snlbanking.service;

import com.snlbanking.dto.AccountCreationDTO;
import com.snlbanking.entity.Account;
import com.snlbanking.entity.User;
import com.snlbanking.exception.DuplicateResourceException;
import com.snlbanking.exception.ResourceNotFoundException;
import com.snlbanking.repository.AccountRepository;
import com.snlbanking.repository.UserRepository;
import com.snlbanking.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for AccountService
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Account Service Tests")
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private User user;
    private Account account;
    private AccountCreationDTO creationDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();

        account = Account.builder()
                .accountId(1L)
                .accountNumber("SNL1234567890")
                .user(user)
                .accountType(Account.AccountType.SAVINGS)
                .balance(new BigDecimal("10000.00"))
                .minimumBalance(new BigDecimal("1000.00"))
                .status(Account.AccountStatus.ACTIVE)
                .ifscCode("HDFC0001234")
                .micRCode("HDFC000123")
                .build();

        creationDTO = AccountCreationDTO.builder()
                .userId(1L)
                .accountType("SAVINGS")
                .ifscCode("HDFC0001234")
                .micRCode("HDFC000123")
                .build();
    }

    @Test
    @DisplayName("Create Account - Success Case")
    void testCreateAccount_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.existsByAccountNumber(any(String.class))).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        var response = accountService.createAccount(creationDTO);

        assertNotNull(response);
        assertEquals("SAVINGS", response.getAccountType().toString());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("Create Account - User Not Found")
    void testCreateAccount_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.createAccount(creationDTO));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Get Account By ID - Success Case")
    void testGetAccountById_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        var response = accountService.getAccountById(1L);

        assertNotNull(response);
        assertEquals("SNL1234567890", response.getAccountNumber());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get Account By ID - Not Found")
    void testGetAccountById_NotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountById(1L));
    }

    @Test
    @DisplayName("Get Account Balance - Success Case")
    void testGetAccountBalance_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        BigDecimal balance = accountService.getAccountBalance(1L);

        assertEquals(new BigDecimal("10000.00"), balance);
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Close Account - Success Case")
    void testCloseAccount_Success() {
        Account closableAccount = Account.builder()
                .accountId(1L)
                .accountNumber("SNL1234567890")
                .user(user)
                .accountType(Account.AccountType.SAVINGS)
                .balance(BigDecimal.ZERO)
                .minimumBalance(new BigDecimal("1000.00"))
                .status(Account.AccountStatus.ACTIVE)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(closableAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(closableAccount);

        assertDoesNotThrow(() -> accountService.closeAccount(1L));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("Freeze Account - Success Case")
    void testFreezeAccount_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        assertDoesNotThrow(() -> accountService.freezeAccount(1L));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("Unfreeze Account - Success Case")
    void testUnfreezeAccount_Success() {
        account.setStatus(Account.AccountStatus.FROZEN);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        assertDoesNotThrow(() -> accountService.unfreezeAccount(1L));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("Generate Account Number - Unique")
    void testGenerateAccountNumber_Unique() {
        String accountNumber1 = accountService.generateAccountNumber();
        String accountNumber2 = accountService.generateAccountNumber();

        assertNotNull(accountNumber1);
        assertNotNull(accountNumber2);
        assertNotEquals(accountNumber1, accountNumber2);
        assertEquals(20, accountNumber1.length());
    }
}
