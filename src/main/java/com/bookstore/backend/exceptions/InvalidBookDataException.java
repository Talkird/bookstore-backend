package com.bookstore.backend.exceptions;

public class InvalidBookDataException extends RuntimeException{
    public InvalidBookDataException(String message){
        super(message);
    }

}
