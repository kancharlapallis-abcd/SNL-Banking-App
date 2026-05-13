package com.snlbanking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Transaction Entity - Represents a bank transaction
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable = false, unique = true, length = 50)
    private String transactionReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Account toAccount;

    @Positive(message = "Amount must be greater than zero")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(length = 500)
    private String description;

    @Column(length = 100)
    private String referenceNumber;

    @Column(precision = 15, scale = 2)
    private BigDecimal charges;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(length = 100)
    private String createdBy;

    /**
     * Transaction Type Enum
     */
    public enum TransactionType {
        INTRA_BANK_TRANSFER, INTER_BANK_TRANSFER, DEPOSIT, WITHDRAWAL, 
        BILL_PAYMENT, LOAN_PAYMENT, INTEREST_CREDIT
    }

    /**
     * Transaction Status Enum
     */
    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED, CANCELLED, REVERSED
    }
}
