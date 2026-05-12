package com.snlbanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Bill Payment Response DTO
 * Used for returning bill payment information
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillPaymentResponseDTO {

    private Long billPaymentId;

    private String billReference;

    private Long accountId;

    private String accountNumber;

    private String billType;

    private String billerName;

    private BigDecimal billAmount;

    private String billerReferenceNumber;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String description;
}
