package com.snlbanking.controller;

import com.snlbanking.dto.APIResponseDTO;
import com.snlbanking.dto.LoanApplicationRequestDTO;
import com.snlbanking.dto.LoanApplicationResponseDTO;
import com.snlbanking.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Loan REST Controller
 * Handles loan REST endpoints
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/v1/loans")
@RequiredArgsConstructor
@Slf4j
public class LoanController {

    private final LoanService loanService;

    /**
     * Submit a loan application
     * @param loanApplicationRequestDTO The loan application request
     * @return ResponseEntity with APIResponseDTO containing loan details
     */
   @PostMapping("/apply")
    public ResponseEntity<APIResponseDTO<LoanApplicationResponseDTO>> submitLoanApplication(
            @Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Received loan application request");

        LoanApplicationResponseDTO loan = loanService.submitLoanApplication(loanApplicationRequestDTO);

        APIResponseDTO<LoanApplicationResponseDTO> response = APIResponseDTO.<LoanApplicationResponseDTO>builder()
                .success(true)
                .message("Loan application submitted successfully")
                .data(loan)
                .statusCode(HttpStatus.CREATED.value()) // FIXED: status -> statusCode
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<APIResponseDTO<LoanApplicationResponseDTO>> getLoanById(
            @PathVariable Long loanId) {
        log.info("Fetching loan with ID: {}", loanId);

        LoanApplicationResponseDTO loan = loanService.getLoanById(loanId);

        APIResponseDTO<LoanApplicationResponseDTO> response = APIResponseDTO.<LoanApplicationResponseDTO>builder()
                .success(true)
                .message("Loan retrieved successfully")
                .data(loan)
                .statusCode(HttpStatus.OK.value()) // FIXED: status -> statusCode
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

   

    /**
     * Get loans for a user
     * @param userId The user ID
     * @param page The page number (default 0)
     * @param size The page size (default 10)
     * @return ResponseEntity with APIResponseDTO containing paginated loans
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<APIResponseDTO<Page<LoanApplicationResponseDTO>>> getUserLoans(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching loans for user ID: {}", userId);

        Pageable pageable = PageRequest.of(page, size);
        Page<LoanApplicationResponseDTO> loans = loanService.getUserLoans(userId, pageable);

        APIResponseDTO<Page<LoanApplicationResponseDTO>> response = APIResponseDTO.<Page<LoanApplicationResponseDTO>>builder()
                .success(true)
                .message("Loans retrieved successfully")
                .data(loans)
                .status(HttpStatus.OK.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all loans
     * @param page The page number (default 0)
     * @param size The page size (default 10)
     * @return ResponseEntity with APIResponseDTO containing paginated loans
     */
    @GetMapping
    public ResponseEntity<APIResponseDTO<Page<LoanApplicationResponseDTO>>> getAllLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching all loans");

        Pageable pageable = PageRequest.of(page, size);
        Page<LoanApplicationResponseDTO> loans = loanService.getAllLoans(pageable);

        APIResponseDTO<Page<LoanApplicationResponseDTO>> response = APIResponseDTO.<Page<LoanApplicationResponseDTO>>builder()
                .success(true)
                .message("Loans retrieved successfully")
                .data(loans)
                .status(HttpStatus.OK.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Approve a loan application
     * @param loanId The loan ID
     * @return ResponseEntity with APIResponseDTO
     */
    @PostMapping("/{loanId}/approve")
    public ResponseEntity<APIResponseDTO<String>> approveLoan(
            @PathVariable Long loanId) {
        log.info("Approving loan with ID: {}", loanId);

        loanService.approveLoan(loanId);

        APIResponseDTO<String> response = APIResponseDTO.<String>builder()
                .success(true)
                .message("Loan approved successfully")
                .data("Loan with ID: " + loanId + " has been approved")
                .status(HttpStatus.OK.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Reject a loan application
     * @param loanId The loan ID
     * @return ResponseEntity with APIResponseDTO
     */
    @PostMapping("/{loanId}/reject")
    public ResponseEntity<APIResponseDTO<String>> rejectLoan(
            @PathVariable Long loanId) {
        log.info("Rejecting loan with ID: {}", loanId);

        loanService.rejectLoan(loanId);

        APIResponseDTO<String> response = APIResponseDTO.<String>builder()
                .success(true)
                .message("Loan rejected successfully")
                .data("Loan with ID: " + loanId + " has been rejected")
                .status(HttpStatus.OK.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get loan by number
     * @param loanNumber The loan number
     * @return ResponseEntity with APIResponseDTO containing loan details
     */
    @GetMapping("/number/{loanNumber}")
    public ResponseEntity<APIResponseDTO<LoanApplicationResponseDTO>> getLoanByNumber(
            @PathVariable String loanNumber) {
        log.info("Fetching loan with number: {}", loanNumber);

        LoanApplicationResponseDTO loan = loanService.getLoanByNumber(loanNumber);

        APIResponseDTO<LoanApplicationResponseDTO> response = APIResponseDTO.<LoanApplicationResponseDTO>builder()
                .success(true)
                .message("Loan retrieved successfully")
                .data(loan)
                .status(HttpStatus.OK.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
