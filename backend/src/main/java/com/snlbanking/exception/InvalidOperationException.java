package com.snlbanking.exception;

/**
 * Custom exception for invalid operation
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
public class InvalidOperationException extends RuntimeException {

    private String operation;
    private String reason;

    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(String operation, String reason) {
        super(String.format("Invalid operation: %s. Reason: %s", operation, reason));
        this.operation = operation;
        this.reason = reason;
    }

    public String getOperation() {
        return operation;
    }

    public String getReason() {
        return reason;
    }
}
