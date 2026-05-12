package com.snlbanking.dto;

import com.snlbanking.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Account DTO for Account Response
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponseDTO {

    private Long accountId;
    private String accountNumber;
    private Account.AccountType accountType;
    private BigDecimal balance;
    private Account.AccountStatus status;
    private String ifscCode;
    private String micRCode;
    private String createdAt;

    public static AccountResponseDTO fromEntity(Account account) {
        return AccountResponseDTO.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .status(account.getStatus())
                .ifscCode(account.getIfscCode())
                .micRCode(account.getMicRCode())
                .createdAt(account.getCreatedAt().toString())
                .build();
    }
}
