package com.snlbanking.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Bill Payment Request DTO
 * Used for submitting bill payment requests
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillPaymentRequestDTO {

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotBlank(message = "Bill type is required")
    private String billType;

    @NotBlank(message = "Biller name is required")
    @Size(min = 2, max = 100, message = "Biller name must be between 2 and 100 characters")
    private String billerName;

    @NotNull(message = "Bill amount is required")
    @DecimalMin(value = "0.01", message = "Bill amount must be greater than 0")
    private BigDecimal billAmount;

    @Size(max = 100, message = "Biller reference number cannot exceed 100 characters")
    private String billerReferenceNumber;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
}
