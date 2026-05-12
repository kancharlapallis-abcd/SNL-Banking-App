package com.snlbanking.controller;

import com.snlbanking.dto.AccountCreationDTO;
import com.snlbanking.dto.AccountResponseDTO;
import com.snlbanking.dto.APIResponseDTO;
import com.snlbanking.service.AccountService;
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

import java.math.BigDecimal;

/**
 * Account REST Controller
 * Handles account creation, management, and balance enquiry
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Account Management", description = "Account creation, management, and balance inquiry APIs")
public class AccountController {

    private final AccountService accountService;

    /**
     * POST /v1/accounts - Create new account
     */
    @PostMapping
    @Operation(summary = "Create a new account", description = "Create a new bank account for a user")
    public ResponseEntity<APIResponseDTO<AccountResponseDTO>> createAccount(
            @Valid @RequestBody AccountCreationDTO creationDTO) {
        log.info("Create account request received for user ID: {}", creationDTO.getUserId());

        AccountResponseDTO accountResponse = accountService.createAccount(creationDTO);

        APIResponseDTO<AccountResponseDTO> response = APIResponseDTO.<AccountResponseDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Account created successfully")
                .data(accountResponse)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /v1/accounts/{id} - Get account by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID", description = "Retrieve account details by account ID")
    public ResponseEntity<APIResponseDTO<AccountResponseDTO>> getAccountById(@PathVariable Long id) {
        log.info("Get account request received for ID: {}", id);

        AccountResponseDTO accountResponse = accountService.getAccountById(id);

        APIResponseDTO<AccountResponseDTO> response = APIResponseDTO.<AccountResponseDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account retrieved successfully")
                .data(accountResponse)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/accounts/number/{accountNumber} - Get account by account number
     */
    @GetMapping("/number/{accountNumber}")
    @Operation(summary = "Get account by account number", description = "Retrieve account details by account number")
    public ResponseEntity<APIResponseDTO<AccountResponseDTO>> getAccountByNumber(
            @PathVariable String accountNumber) {
        log.info("Get account request received for account number: {}", accountNumber);

        AccountResponseDTO accountResponse = AccountResponseDTO.fromEntity(
                accountService.getAccountByNumber(accountNumber));

        APIResponseDTO<AccountResponseDTO> response = APIResponseDTO.<AccountResponseDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account retrieved successfully")
                .data(accountResponse)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/accounts - Get all accounts with pagination
     */
    @GetMapping
    @Operation(summary = "Get all accounts", description = "Retrieve all accounts with pagination support")
    public ResponseEntity<APIResponseDTO<Page<AccountResponseDTO>>> getAllAccounts(Pageable pageable) {
        log.info("Get all accounts request received");

        Page<AccountResponseDTO> accounts = accountService.getAllAccounts(pageable);

        APIResponseDTO<Page<AccountResponseDTO>> response = APIResponseDTO.<Page<AccountResponseDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Accounts retrieved successfully")
                .data(accounts)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/accounts/user/{userId} - Get accounts by user ID
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user's accounts", description = "Retrieve all accounts for a specific user")
    public ResponseEntity<APIResponseDTO<Page<AccountResponseDTO>>> getAccountsByUser(
            @PathVariable Long userId,
            Pageable pageable) {
        log.info("Get accounts request received for user ID: {}", userId);

        Page<AccountResponseDTO> accounts = accountService.getAccountsByUser(userId, pageable);

        APIResponseDTO<Page<AccountResponseDTO>> response = APIResponseDTO.<Page<AccountResponseDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User accounts retrieved successfully")
                .data(accounts)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/accounts/{id}/balance - Get account balance
     */
    @GetMapping("/{id}/balance")
    @Operation(summary = "Get account balance", description = "Retrieve current balance of an account")
    public ResponseEntity<APIResponseDTO<BigDecimal>> getAccountBalance(@PathVariable Long id) {
        log.info("Get balance request received for account ID: {}", id);

        BigDecimal balance = accountService.getAccountBalance(id);

        APIResponseDTO<BigDecimal> response = APIResponseDTO.<BigDecimal>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account balance retrieved successfully")
                .data(balance)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * PUT /v1/accounts/{id} - Update account
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update account", description = "Update account details")
    public ResponseEntity<APIResponseDTO<AccountResponseDTO>> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody AccountCreationDTO updateDTO) {
        log.info("Update account request received for ID: {}", id);

        AccountResponseDTO accountResponse = accountService.updateAccount(id, updateDTO);

        APIResponseDTO<AccountResponseDTO> response = APIResponseDTO.<AccountResponseDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account updated successfully")
                .data(accountResponse)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * POST /v1/accounts/{id}/close - Close account
     */
    @PostMapping("/{id}/close")
    @Operation(summary = "Close account", description = "Close a bank account")
    public ResponseEntity<APIResponseDTO<String>> closeAccount(@PathVariable Long id) {
        log.info("Close account request received for ID: {}", id);

        accountService.closeAccount(id);

        APIResponseDTO<String> response = APIResponseDTO.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account closed successfully")
                .data("Account is now closed")
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * POST /v1/accounts/{id}/freeze - Freeze account
     */
    @PostMapping("/{id}/freeze")
    @Operation(summary = "Freeze account", description = "Temporarily freeze account for security")
    public ResponseEntity<APIResponseDTO<String>> freezeAccount(@PathVariable Long id) {
        log.info("Freeze account request received for ID: {}", id);

        accountService.freezeAccount(id);

        APIResponseDTO<String> response = APIResponseDTO.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account frozen successfully")
                .data("Account is now frozen")
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * POST /v1/accounts/{id}/unfreeze - Unfreeze account
     */
    @PostMapping("/{id}/unfreeze")
    @Operation(summary = "Unfreeze account", description = "Unfreeze a frozen account")
    public ResponseEntity<APIResponseDTO<String>> unfreezeAccount(@PathVariable Long id) {
        log.info("Unfreeze account request received for ID: {}", id);

        accountService.unfreezeAccount(id);

        APIResponseDTO<String> response = APIResponseDTO.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account unfrozen successfully")
                .data("Account is now active")
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }
}
