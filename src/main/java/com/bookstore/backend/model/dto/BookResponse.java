package com.bookstore.backend.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.bookstore.backend.model.book.Book;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponse {
    private Long id;
    private Long isbn;
    private String title;
    private String author;
    private int year;
    private double price;
    private int stock;
    private String genre;

    public static BookResponse convertToBookResponse(Book book) {
    return BookResponse.builder()
            .id(book.getId())
            .isbn(book.getIsbn())
            .title(book.getTitle())
            .author(book.getAuthor())
            .year(book.getYear())
            .price(book.getPrice())
            .stock(book.getStock())
            .genre(book.getGenre().toString())
            .build();
    }

    public static List<BookResponse> convertToBookResponse(List<Book> books) {
        List<BookResponse> bookResponses = new ArrayList<>();
        books.forEach(book -> bookResponses.add(convertToBookResponse(book)));
        return bookResponses;
    }  
}
