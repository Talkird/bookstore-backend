package com.bookstore.backend.exception.book;

public class InvalidBookDataException extends RuntimeException{
    public InvalidBookDataException(String message){
        super(message);
    }

}
