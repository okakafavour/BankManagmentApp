package org.example.exception;

public class WrongAccountTypeException extends RuntimeException {
    public WrongAccountTypeException(String message) {
        super(message);
    }
}
