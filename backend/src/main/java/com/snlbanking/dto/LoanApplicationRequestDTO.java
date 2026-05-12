package com.snlbanking.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Loan Application Request DTO
 * Used for submitting loan applications
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Loan type is required")
    private String loanType;

    @NotNull(message = "Principal amount is required")
    @DecimalMin(value = "10000.00", message = "Principal amount must be at least 10000")
    @DecimalMax(value = "5000000.00", message = "Principal amount cannot exceed 5000000")
    private BigDecimal principalAmount;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "1.0", message = "Interest rate must be at least 1%")
    @DecimalMax(value = "20.0", message = "Interest rate cannot exceed 20%")
    private Double interestRate;

    @NotNull(message = "Tenure is required")
    @Min(value = 6, message = "Tenure must be at least 6 months")
    @Max(value = 360, message = "Tenure cannot exceed 30 years (360 months)")
    private Integer tenureMonths;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
}
