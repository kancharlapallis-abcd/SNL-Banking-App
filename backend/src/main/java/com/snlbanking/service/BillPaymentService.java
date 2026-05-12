package com.snlbanking.service;

import com.snlbanking.dto.BillPaymentRequestDTO;
import com.snlbanking.dto.BillPaymentResponseDTO;
import com.snlbanking.entity.BillPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Bill Payment Service Interface
 * Manages bill payment operations
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
public interface BillPaymentService {

    /**
     * Pay a bill and deduct from account
     * @param billPaymentRequestDTO The bill payment request
     * @return BillPaymentResponseDTO with payment details
     */
    BillPaymentResponseDTO payBill(BillPaymentRequestDTO billPaymentRequestDTO);

    /**
     * Get bill payment by ID
     * @param billPaymentId The bill payment ID
     * @return BillPaymentResponseDTO
     */
    BillPaymentResponseDTO getBillPaymentById(Long billPaymentId);

    /**
     * Get all bill payments for an account
     * @param accountId The account ID
     * @param pageable Pagination info
     * @return Page of bill payments
     */
    Page<BillPaymentResponseDTO> getAccountBillPayments(Long accountId, Pageable pageable);

    /**
     * Get all bill payments
     * @param pageable Pagination info
     * @return Page of bill payments
     */
    Page<BillPaymentResponseDTO> getAllBillPayments(Pageable pageable);

    /**
     * Cancel a pending bill payment
     * @param billPaymentId The bill payment ID
     */
    void cancelBillPayment(Long billPaymentId);

    /**
     * Get bill payment by reference
     * @param billReference The bill reference number
     * @return BillPaymentResponseDTO
     */
    BillPaymentResponseDTO getBillPaymentByReference(String billReference);
}
