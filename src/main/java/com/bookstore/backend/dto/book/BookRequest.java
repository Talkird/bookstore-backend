package com.bookstore.backend.dto.book;

import com.bookstore.backend.model.book.Genre;

import lombok.Data;

@Data
public class BookRequest {
    private Long id;
    private Long isbn;
    private String title;
    private String author;
    private int year;
    private double price;
    private Genre genre;
}
