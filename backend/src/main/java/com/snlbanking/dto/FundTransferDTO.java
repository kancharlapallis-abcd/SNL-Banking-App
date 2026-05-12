package com.snlbanking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Fund Transfer DTO
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundTransferDTO {

    @NotNull(message = "From Account ID is required")
    private Long fromAccountId;

    @NotBlank(message = "To Account Number is required")
    private String toAccountNumber;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    //@NotBlank(message = "Description is required")
    private String description;

    private String referenceNumber;
}
