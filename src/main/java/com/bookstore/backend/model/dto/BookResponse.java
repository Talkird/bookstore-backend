package com.bookstore.backend.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.bookstore.backend.model.book.Book;
import com.bookstore.backend.model.book.Genre;

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
    private Genre genre;
    private String description;
    private double rating;

    public static BookResponse convertToBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .year(book.getYear())
                .price(book.getPrice())
                .stock(book.getStock())
                .genre(book.getGenre())
                .description(book.getDescription())
                .rating(book.getRating())
                .build();
    }

    public static List<BookResponse> convertToBookResponse(List<Book> books) {
        List<BookResponse> bookResponses = new ArrayList<>();
        books.forEach(book -> bookResponses.add(convertToBookResponse(book)));
        return bookResponses;
    }

    public static Book convertToBook(BookResponse bookResponse) {
        Book book = new Book();
        book.setId(bookResponse.getId());
        book.setIsbn(bookResponse.getIsbn());
        book.setTitle(bookResponse.getTitle());
        book.setAuthor(bookResponse.getAuthor());
        book.setYear(bookResponse.getYear());
        book.setPrice(bookResponse.getPrice());
        book.setStock(bookResponse.getStock());
        book.setGenre(bookResponse.getGenre());
        book.setDescription(bookResponse.getDescription());
        book.setRating(bookResponse.getRating());
        return book;
    }
}
