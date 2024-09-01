package com.bookstore.backend.exception.auth;

public class InvalidDiscountCodeException extends RuntimeException {

    public InvalidDiscountCodeException(String message) {
        super(message);
    }
    
    public InvalidDiscountCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
