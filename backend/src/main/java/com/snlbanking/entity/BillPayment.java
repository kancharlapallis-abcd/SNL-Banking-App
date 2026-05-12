package com.snlbanking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Bill Payment Entity - Represents a bill payment record
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Entity
@Table(name = "bill_payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class BillPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billPaymentId;

    @Column(nullable = false, unique = true, length = 50)
    private String billReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillType billType;

    @Column(nullable = false, length = 100)
    private String billerName;

    @Positive(message = "Bill amount must be greater than zero")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal billAmount;

    @Column(length = 100)
    private String billerReferenceNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillPaymentStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Bill Type Enum
     */
    public enum BillType {
        ELECTRICITY, WATER, GAS, INTERNET, MOBILE, CREDIT_CARD, INSURANCE, LOAN
    }

    /**
     * Bill Payment Status Enum
     */
    public enum BillPaymentStatus {
        PENDING, COMPLETED, FAILED, CANCELLED
    }
}
