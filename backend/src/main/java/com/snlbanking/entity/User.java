package com.snlbanking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * User Entity - Represents a bank customer or user in the system
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "First name is required")
    @Column(nullable = false, length = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(nullable = false, length = 100)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid", 
            regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be 10 digits")
    @Column(unique = true, length = 10)
    private String mobileNumber;

    @Column(length = 100)
    private String panNumber;

    @Column(length = 12)
    private String aadharNumber;

    @Column(length = 255)
    private String address;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String state;

    @Pattern(regexp = "^\\d{6}$", message = "Pincode must be 6 digits")
    @Column(length = 6)
    private String pincode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(length = 100)
    private String createdBy;

    @Column(length = 100)
    private String updatedBy;
    
@PrePersist
protected void onCreate() {
    this.createdAt = java.time.LocalDateTime.now();
    this.updatedAt = java.time.LocalDateTime.now();
}
    /**
     * User Role Enum
     */
    public enum UserRole {
        CUSTOMER, ADMIN, SUPPORT_STAFF
    }

    /**
     * User Status Enum
     */
    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED, VERIFIED_PENDING
    }
}
