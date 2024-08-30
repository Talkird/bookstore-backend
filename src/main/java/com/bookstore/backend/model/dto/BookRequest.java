package com.bookstore.backend.model.dto;

import org.antlr.v4.runtime.misc.NotNull;

import com.bookstore.backend.model.Genre;
import lombok.Data;

@Data
public class BookRequest {
    private Long id;

    @SuppressWarnings("deprecation")
    @NotNull
    private Long isbn;

    
    private String title;
    private String author;
    private int year;
    private double price;
    private Genre genre;
}
