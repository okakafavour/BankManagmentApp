package org.example.exception;

public class AccountBalanceException extends RuntimeException {
    public AccountBalanceException(String message) {
        super(message);
    }
}
