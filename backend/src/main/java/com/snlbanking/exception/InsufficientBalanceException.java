package com.snlbanking.exception;

/**
 * Custom exception for insufficient balance
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
public class InsufficientBalanceException extends RuntimeException {

    private String accountNumber;
    private Double availableBalance;
    private Double requestedAmount;

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(String accountNumber, Double availableBalance, Double requestedAmount) {
        super(String.format("Insufficient balance in account %s. Available: %s, Requested: %s",
                accountNumber, availableBalance, requestedAmount));
        this.accountNumber = accountNumber;
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Double getAvailableBalance() {
        return availableBalance;
    }

    public Double getRequestedAmount() {
        return requestedAmount;
    }
}
