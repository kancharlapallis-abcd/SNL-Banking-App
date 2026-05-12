package com.snlbanking.dto;

import com.snlbanking.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User DTO for User Response
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String panNumber;
    private String aadharNumber;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private User.UserRole role;
    private User.UserStatus status;
    private String createdAt;
    private String updatedAt;
    
    // ADD THIS LINE
    private String token; 

    public static UserResponseDTO fromEntity(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .panNumber(user.getPanNumber())
                .aadharNumber(user.getAadharNumber())
                .address(user.getAddress())
                .city(user.getCity())
                .state(user.getState())
                .pincode(user.getPincode())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null)
                .updatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null)
                .build();
    }
}