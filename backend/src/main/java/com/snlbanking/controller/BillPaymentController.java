package com.snlbanking.controller;

import com.snlbanking.dto.APIResponseDTO;
import com.snlbanking.dto.BillPaymentRequestDTO;
import com.snlbanking.dto.BillPaymentResponseDTO;
import com.snlbanking.service.BillPaymentService;
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
 * Bill Payment REST Controller
 * Handles bill payment REST endpoints
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/v1/bills")
@RequiredArgsConstructor
@Slf4j
public class BillPaymentController {

    private final BillPaymentService billPaymentService;

    /**
     * Pay a bill
     * @param billPaymentRequestDTO The bill payment request
     * @return ResponseEntity with APIResponseDTO containing bill payment details
     */
    @PostMapping("/pay")
    public ResponseEntity<APIResponseDTO<BillPaymentResponseDTO>> payBill(
            @Valid @RequestBody BillPaymentRequestDTO billPaymentRequestDTO) {
        log.info("Received bill payment request");

        BillPaymentResponseDTO billPayment = billPaymentService.payBill(billPaymentRequestDTO);

        APIResponseDTO<BillPaymentResponseDTO> response = APIResponseDTO.<BillPaymentResponseDTO>builder()
                .success(true)
                .message("Bill payment processed successfully")
                .data(billPayment)
                .status(HttpStatus.CREATED.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get bill payment by ID
     * @param billPaymentId The bill payment ID
     * @return ResponseEntity with APIResponseDTO containing bill payment details
     */
    @GetMapping("/{billPaymentId}")
    public ResponseEntity<APIResponseDTO<BillPaymentResponseDTO>> getBillPaymentById(
            @PathVariable Long billPaymentId) {
        log.info("Fetching bill payment with ID: {}", billPaymentId);

        BillPaymentResponseDTO billPayment = billPaymentService.getBillPaymentById(billPaymentId);

        APIResponseDTO<BillPaymentResponseDTO> response = APIResponseDTO.<BillPaymentResponseDTO>builder()
                .success(true)
                .message("Bill payment retrieved successfully")
                .data(billPayment)
                .status(HttpStatus.OK.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get bill payments for an account
     * @param accountId The account ID
     * @param page The page number (default 0)
     * @param size The page size (default 10)
     * @return ResponseEntity with APIResponseDTO containing paginated bill payments
     */
    @GetMapping("/account/{accountId}")
    public ResponseEntity<APIResponseDTO<Page<BillPaymentResponseDTO>>> getAccountBillPayments(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching bill payments for account ID: {}", accountId);

        Pageable pageable = PageRequest.of(page, size);
        Page<BillPaymentResponseDTO> billPayments = billPaymentService.getAccountBillPayments(accountId, pageable);

        APIResponseDTO<Page<BillPaymentResponseDTO>> response = APIResponseDTO.<Page<BillPaymentResponseDTO>>builder()
                .success(true)
                .message("Bill payments retrieved successfully")
                .data(billPayments)
                .status(HttpStatus.OK.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all bill payments
     * @param page The page number (default 0)
     * @param size The page size (default 10)
     * @return ResponseEntity with APIResponseDTO containing paginated bill payments
     */
    @GetMapping
    public ResponseEntity<APIResponseDTO<Page<BillPaymentResponseDTO>>> getAllBillPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching all bill payments");

        Pageable pageable = PageRequest.of(page, size);
        Page<BillPaymentResponseDTO> billPayments = billPaymentService.getAllBillPayments(pageable);

        APIResponseDTO<Page<BillPaymentResponseDTO>> response = APIResponseDTO.<Page<BillPaymentResponseDTO>>builder()
                .success(true)
                .message("Bill payments retrieved successfully")
                .data(billPayments)
                .status(HttpStatus.OK.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Cancel a bill payment
     * @param billPaymentId The bill payment ID
     * @return ResponseEntity with APIResponseDTO
     */
    @PostMapping("/{billPaymentId}/cancel")
    public ResponseEntity<APIResponseDTO<String>> cancelBillPayment(
            @PathVariable Long billPaymentId) {
        log.info("Cancelling bill payment with ID: {}", billPaymentId);

        billPaymentService.cancelBillPayment(billPaymentId);

        APIResponseDTO<String> response = APIResponseDTO.<String>builder()
                .success(true)
                .message("Bill payment cancelled successfully")
                .data("Bill payment with ID: " + billPaymentId + " has been cancelled")
                .status(HttpStatus.OK.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get bill payment by reference
     * @param billReference The bill reference number
     * @return ResponseEntity with APIResponseDTO containing bill payment details
     */
    @GetMapping("/reference/{billReference}")
    public ResponseEntity<APIResponseDTO<BillPaymentResponseDTO>> getBillPaymentByReference(
            @PathVariable String billReference) {
        log.info("Fetching bill payment with reference: {}", billReference);

        BillPaymentResponseDTO billPayment = billPaymentService.getBillPaymentByReference(billReference);

        APIResponseDTO<BillPaymentResponseDTO> response = APIResponseDTO.<BillPaymentResponseDTO>builder()
                .success(true)
                .message("Bill payment retrieved successfully")
                .data(billPayment)
                .status(HttpStatus.OK.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
