package com.bookstore.backend.exceptions;

public class InvalidDiscountCodeException extends RuntimeException {

    public InvalidDiscountCodeException(String message) {
        super(message);
    }
    
    public InvalidDiscountCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
