    package com.snlbanking.service;

    import com.snlbanking.dto.AccountCreationDTO;
    import com.snlbanking.dto.AccountResponseDTO;
    import com.snlbanking.entity.Account;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;

    import java.math.BigDecimal;

    /**
     * Account Service Interface
     * Defines business logic operations for Account entity
     * 
     * @author SNL Banking Team
     * @version 1.0.0
     */
    public interface AccountService {

        /**
         * Create a new bank account
         */
        AccountResponseDTO createAccount(AccountCreationDTO creationDTO);

        /**
         * Get account by ID
         */
        AccountResponseDTO getAccountById(Long accountId);

        /**
         * Get account by account number
         */
        Account getAccountByNumber(String accountNumber);

        /**
         * Get all accounts for a user
         */
        Page<AccountResponseDTO> getAccountsByUser(Long userId, Pageable pageable);

        /**
         * Get account balance
         */
        BigDecimal getAccountBalance(Long accountId);

        /**
         * Update account information
         */
        AccountResponseDTO updateAccount(Long accountId, AccountCreationDTO updateDTO);

        /**
         * Close account
         */
        void closeAccount(Long accountId);

        /**
         * Freeze account (temporary block)
         */
        void freezeAccount(Long accountId);

        /**
         * Unfreeze account
         */
        void unfreezeAccount(Long accountId);

        /**
         * Get all accounts with pagination
         */
        Page<AccountResponseDTO> getAllAccounts(Pageable pageable);

        /**
         * Get accounts by type
         */
        Page<AccountResponseDTO> getAccountsByType(String accountType, Pageable pageable);

        /**
         * Generate unique account number
         */
        String generateAccountNumber();
    }
