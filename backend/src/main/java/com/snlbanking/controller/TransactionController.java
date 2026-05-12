package com.snlbanking.controller;

import com.snlbanking.dto.APIResponseDTO;
import com.snlbanking.dto.FundTransferDTO;
import com.snlbanking.entity.Transaction;
import com.snlbanking.service.TransactionService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Transaction REST Controller
 * Handles fund transfers and transaction inquiries
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Transaction Management", description = "Fund transfer, transaction history, and balance inquiry APIs")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * POST /v1/transactions/transfer - Transfer funds
     */
    @PostMapping("/transfer")
    @Operation(summary = "Transfer funds", description = "Transfer funds between bank accounts")
    public ResponseEntity<APIResponseDTO<Transaction>> transferFunds(
            @Valid @RequestBody FundTransferDTO transferDTO) {
        log.info("Fund transfer request received from account ID: {}", transferDTO.getFromAccountId());

        Transaction transaction = transactionService.transferFunds(transferDTO);

        APIResponseDTO<Transaction> response = APIResponseDTO.<Transaction>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Fund transfer completed successfully")
                .data(transaction)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /v1/transactions/{id} - Get transaction by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID", description = "Retrieve transaction details by transaction ID")
    public ResponseEntity<APIResponseDTO<Transaction>> getTransactionById(@PathVariable Long id) {
        log.info("Get transaction request received for ID: {}", id);

        Transaction transaction = transactionService.getTransactionById(id);

        APIResponseDTO<Transaction> response = APIResponseDTO.<Transaction>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Transaction retrieved successfully")
                .data(transaction)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/transactions/reference/{reference} - Get transaction by reference
     */
    @GetMapping("/reference/{reference}")
    @Operation(summary = "Get transaction by reference", description = "Retrieve transaction details by transaction reference number")
    public ResponseEntity<APIResponseDTO<Transaction>> getTransactionByReference(
            @PathVariable String reference) {
        log.info("Get transaction request received for reference: {}", reference);

        Transaction transaction = transactionService.getTransactionByReference(reference);

        APIResponseDTO<Transaction> response = APIResponseDTO.<Transaction>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Transaction retrieved successfully")
                .data(transaction)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/transactions - Get all transactions with pagination
     */
    @GetMapping
    @Operation(summary = "Get all transactions", description = "Retrieve all transactions with pagination support")
    public ResponseEntity<APIResponseDTO<Page<Transaction>>> getAllTransactions(Pageable pageable) {
        log.info("Get all transactions request received");

        Page<Transaction> transactions = transactionService.getAllTransactions(pageable);

        APIResponseDTO<Page<Transaction>> response = APIResponseDTO.<Page<Transaction>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Transactions retrieved successfully")
                .data(transactions)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/transactions/account/{accountId} - Get account transactions
     */
    @GetMapping("/account/{accountId}")
    @Operation(summary = "Get account transactions", description = "Retrieve all transactions for a specific account")
    public ResponseEntity<APIResponseDTO<Page<Transaction>>> getAccountTransactions(
            @PathVariable Long accountId,
            Pageable pageable) {
        log.info("Get account transactions request received for account ID: {}", accountId);

        Page<Transaction> transactions = transactionService.getAccountTransactions(accountId, pageable);

        APIResponseDTO<Page<Transaction>> response = APIResponseDTO.<Page<Transaction>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account transactions retrieved successfully")
                .data(transactions)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/transactions/account/{accountId}/outgoing - Get outgoing transactions
     */
    @GetMapping("/account/{accountId}/outgoing")
    @Operation(summary = "Get outgoing transactions", description = "Retrieve all outgoing transactions for an account")
    public ResponseEntity<APIResponseDTO<Page<Transaction>>> getOutgoingTransactions(
            @PathVariable Long accountId,
            Pageable pageable) {
        log.info("Get outgoing transactions request received for account ID: {}", accountId);

        Page<Transaction> transactions = transactionService.getOutgoingTransactions(accountId, pageable);

        APIResponseDTO<Page<Transaction>> response = APIResponseDTO.<Page<Transaction>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Outgoing transactions retrieved successfully")
                .data(transactions)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/transactions/account/{accountId}/incoming - Get incoming transactions
     */
    @GetMapping("/account/{accountId}/incoming")
    @Operation(summary = "Get incoming transactions", description = "Retrieve all incoming transactions for an account")
    public ResponseEntity<APIResponseDTO<Page<Transaction>>> getIncomingTransactions(
            @PathVariable Long accountId,
            Pageable pageable) {
        log.info("Get incoming transactions request received for account ID: {}", accountId);

        Page<Transaction> transactions = transactionService.getIncomingTransactions(accountId, pageable);

        APIResponseDTO<Page<Transaction>> response = APIResponseDTO.<Page<Transaction>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Incoming transactions retrieved successfully")
                .data(transactions)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/transactions/account/{accountId}/history - Get transaction history
     */
    @GetMapping("/account/{accountId}/history")
    @Operation(summary = "Get transaction history", description = "Retrieve transaction history for an account within date range")
    public ResponseEntity<APIResponseDTO<Page<Transaction>>> getTransactionHistory(
            @PathVariable Long accountId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            Pageable pageable) {
        log.info("Get transaction history request received for account ID: {}", accountId);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);

        Page<Transaction> transactions = transactionService.getTransactionsByDateRange(accountId, start, end, pageable);

        APIResponseDTO<Page<Transaction>> response = APIResponseDTO.<Page<Transaction>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Transaction history retrieved successfully")
                .data(transactions)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * POST /v1/transactions/{id}/reverse - Reverse transaction
     */
    @PostMapping("/{id}/reverse")
    @Operation(summary = "Reverse transaction", description = "Reverse a completed transaction")
    public ResponseEntity<APIResponseDTO<String>> reverseTransaction(@PathVariable Long id) {
        log.info("Reverse transaction request received for ID: {}", id);

        transactionService.reverseTransaction(id);

        APIResponseDTO<String> response = APIResponseDTO.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Transaction reversed successfully")
                .data("Transaction has been reversed")
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/transactions/account/{accountId}/total-outgoing - Calculate total outgoing
     */
    @GetMapping("/account/{accountId}/total-outgoing")
    @Operation(summary = "Get total outgoing amount", description = "Calculate total outgoing amount for an account")
    public ResponseEntity<APIResponseDTO<BigDecimal>> calculateTotalOutgoing(@PathVariable Long accountId) {
        log.info("Calculate total outgoing request received for account ID: {}", accountId);

        BigDecimal totalOutgoing = transactionService.calculateTotalOutgoing(accountId);

        APIResponseDTO<BigDecimal> response = APIResponseDTO.<BigDecimal>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Total outgoing calculated successfully")
                .data(totalOutgoing)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }
}
