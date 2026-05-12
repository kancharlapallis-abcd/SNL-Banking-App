package com.snlbanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API Response DTO
 * Generic response wrapper for all API responses
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class APIResponseDTO<T> {
// Keep this for existing Phase 1 code
    private int statusCode; 
    
    // ADD THIS for the new Phase 2 code
    private int status;     
    
    private boolean success;
    private String message;
    private T data;
    private String timestamp;
    private String path;

    // Update your constructor to handle both
    public APIResponseDTO(int statusCode, boolean success, String message, T data) {
        this.statusCode = statusCode;
        this.status = statusCode; // Sync both fields
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }
}
