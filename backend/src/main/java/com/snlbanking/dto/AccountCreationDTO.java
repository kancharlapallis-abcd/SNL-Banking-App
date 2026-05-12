package com.snlbanking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Account Creation DTO
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCreationDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Account type is required")
    private String accountType;

    private String ifscCode;

    
    private String micRCode;
}
