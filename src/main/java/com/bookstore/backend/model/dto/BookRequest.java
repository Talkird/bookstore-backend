package com.bookstore.backend.model.dto;

import com.bookstore.backend.model.Genre;

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
