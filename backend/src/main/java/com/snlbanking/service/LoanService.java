package com.snlbanking.service;

import com.snlbanking.dto.LoanApplicationRequestDTO;
import com.snlbanking.dto.LoanApplicationResponseDTO;
import com.snlbanking.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Loan Service Interface
 * Manages loan operations
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
public interface LoanService {

    /**
     * Submit a loan application
     * @param loanApplicationRequestDTO The loan application request
     * @return LoanApplicationResponseDTO with application details
     */
    LoanApplicationResponseDTO submitLoanApplication(LoanApplicationRequestDTO loanApplicationRequestDTO);

    /**
     * Get loan by ID
     * @param loanId The loan ID
     * @return LoanApplicationResponseDTO
     */
    LoanApplicationResponseDTO getLoanById(Long loanId);

    /**
     * Get all loans for a user
     * @param userId The user ID
     * @param pageable Pagination info
     * @return Page of loans
     */
    Page<LoanApplicationResponseDTO> getUserLoans(Long userId, Pageable pageable);

    /**
     * Get all loans
     * @param pageable Pagination info
     * @return Page of loans
     */
    Page<LoanApplicationResponseDTO> getAllLoans(Pageable pageable);

    /**
     * Approve a loan application
     * @param loanId The loan ID
     */
    void approveLoan(Long loanId);

    /**
     * Reject a loan application
     * @param loanId The loan ID
     */
    void rejectLoan(Long loanId);

    /**
     * Get loan by number
     * @param loanNumber The loan number
     * @return LoanApplicationResponseDTO
     */
    LoanApplicationResponseDTO getLoanByNumber(String loanNumber);
}
