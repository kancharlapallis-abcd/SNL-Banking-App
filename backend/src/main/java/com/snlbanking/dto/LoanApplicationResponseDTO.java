package com.snlbanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Loan Application Response DTO
 * Used for returning loan application information
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationResponseDTO {

    private Long loanApplicationId;

    private String loanNumber;

    private Long userId;

    private String userFirstName;

    private String userLastName;

    private String loanType;

    private BigDecimal principalAmount;

    private Double interestRate;

    private Integer tenureMonths;

    private BigDecimal emiAmount;

    private BigDecimal remainingAmount;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String description;
}
