package com.bookstore.backend.exception.auth;

public class JwtTokenMalformedException extends RuntimeException {
    public JwtTokenMalformedException(String message) {
        super(message);
    }
}
