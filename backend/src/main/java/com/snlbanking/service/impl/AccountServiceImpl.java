package com.snlbanking.service.impl;

import com.snlbanking.dto.AccountCreationDTO;
import com.snlbanking.dto.AccountResponseDTO;
import com.snlbanking.entity.Account;
import com.snlbanking.entity.User;
import com.snlbanking.exception.DuplicateResourceException;
import com.snlbanking.exception.InvalidOperationException;
import com.snlbanking.exception.ResourceNotFoundException;
import com.snlbanking.repository.AccountRepository;
import com.snlbanking.repository.UserRepository;
import com.snlbanking.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Account Service Implementation
 * Implements business logic for Account entity
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    private static final BigDecimal SAVINGS_MIN_BALANCE = new BigDecimal("1000.00");
    private static final BigDecimal CURRENT_MIN_BALANCE = new BigDecimal("5000.00");
    private static final BigDecimal SALARY_MIN_BALANCE = new BigDecimal("500.00");

    @Override
    public AccountResponseDTO createAccount(AccountCreationDTO creationDTO) {
        log.info("Creating new account for user ID: {}", creationDTO.getUserId());

        // Validate input
        if (creationDTO == null || creationDTO.getUserId() == null) {
            throw new IllegalArgumentException("Account creation data is invalid");
        }

        // Get user
        User user = userRepository.findById(creationDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", creationDTO.getUserId()));

        // Generate unique account number
        String accountNumber = generateAccountNumber();

        // Check if account number already exists
        if (accountRepository.existsByAccountNumber(accountNumber)) {
            log.warn("Account number {} already exists", accountNumber);
            throw new DuplicateResourceException("Account", "accountNumber", accountNumber);
        }

        // Determine minimum balance based on account type
        Account.AccountType accountType = Account.AccountType.valueOf(creationDTO.getAccountType());
        BigDecimal minimumBalance = getMinimumBalance(accountType);

        // Create new account
       // Create new account
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .user(user)
                .accountType(accountType)
                // CHANGE THIS: Use minimumBalance instead of ZERO to satisfy the @Positive constraint
                .balance(minimumBalance) 
                .minimumBalance(minimumBalance)
                .status(Account.AccountStatus.ACTIVE)
                .ifscCode(creationDTO.getIfscCode())
                .micRCode(creationDTO.getMicRCode())
                // Ensure timestamps are set if they aren't handled by @PrePersist
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build();

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with account number: {}", accountNumber);

        return AccountResponseDTO.fromEntity(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDTO getAccountById(Long accountId) {
        log.debug("Fetching account with ID: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));

        return AccountResponseDTO.fromEntity(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Account getAccountByNumber(String accountNumber) {
        log.debug("Fetching account with number: {}", accountNumber);

        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty");
        }

        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "accountNumber", accountNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountResponseDTO> getAccountsByUser(Long userId, Pageable pageable) {
        log.debug("Fetching accounts for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return accountRepository.findByUser(user, pageable)
                .map(AccountResponseDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAccountBalance(Long accountId) {
        log.debug("Fetching balance for account ID: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));

        return account.getBalance();
    }

    @Override
    public AccountResponseDTO updateAccount(Long accountId, AccountCreationDTO updateDTO) {
        log.info("Updating account ID: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));

        account.setIfscCode(updateDTO.getIfscCode());
        account.setMicRCode(updateDTO.getMicRCode());

        Account updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully with ID: {}", accountId);

        return AccountResponseDTO.fromEntity(updatedAccount);
    }

    @Override
    public void closeAccount(Long accountId) {
        log.info("Closing account ID: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));

        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            log.warn("Cannot close account with remaining balance");
            throw new InvalidOperationException("closeAccount", "Account has remaining balance");
        }

        account.setStatus(Account.AccountStatus.CLOSED);
        accountRepository.save(account);
        log.info("Account closed successfully with ID: {}", accountId);
    }

    @Override
    public void freezeAccount(Long accountId) {
        log.info("Freezing account ID: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));

        account.setStatus(Account.AccountStatus.FROZEN);
        accountRepository.save(account);
        log.info("Account frozen successfully with ID: {}", accountId);
    }

    @Override
    public void unfreezeAccount(Long accountId) {
        log.info("Unfreezing account ID: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));

        account.setStatus(Account.AccountStatus.ACTIVE);
        accountRepository.save(account);
        log.info("Account unfrozen successfully with ID: {}", accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountResponseDTO> getAllAccounts(Pageable pageable) {
        log.debug("Fetching all accounts with pagination");

        return accountRepository.findAll(pageable)
                .map(AccountResponseDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountResponseDTO> getAccountsByType(String accountType, Pageable pageable) {
        log.debug("Fetching accounts by type: {}", accountType);

        Account.AccountType type = Account.AccountType.valueOf(accountType);
        return accountRepository.findByAccountType(type, pageable)
                .map(AccountResponseDTO::fromEntity);
    }

    @Override
    public String generateAccountNumber() {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        String accountNumber = "SNL" + System.currentTimeMillis() + uuid;
        return accountNumber.substring(0, 20);
    }

    /**
     * Determine minimum balance based on account type
     */
    private BigDecimal getMinimumBalance(Account.AccountType accountType) {
        return switch (accountType) {
            case SAVINGS -> SAVINGS_MIN_BALANCE;
            case CURRENT -> CURRENT_MIN_BALANCE;
            case SALARY -> SALARY_MIN_BALANCE;  
        };
    }
}
