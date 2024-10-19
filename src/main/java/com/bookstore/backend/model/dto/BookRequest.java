package com.bookstore.backend.model.dto;

import com.bookstore.backend.model.book.Book;
import com.bookstore.backend.model.book.Genre;
import lombok.Builder;
import lombok.Data;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class BookRequest {
    private Long id;
    private Long isbn;
    private String title;
    private String author;
    private int year;
    private double price;
    private int stock;
    private Genre genre;
    private String description;
    private MultipartFile image; 

    public static Book convertToBook(BookRequest bookRequest) {
        Book book = new Book();
        book.setId(bookRequest.getId());
        book.setIsbn(bookRequest.getIsbn());
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setYear(bookRequest.getYear());
        book.setPrice(bookRequest.getPrice());
        book.setStock(bookRequest.getStock());
        book.setGenre(bookRequest.getGenre());
        book.setDescription(bookRequest.getDescription());

        // Convert MultipartFile to byte[]
        if (bookRequest.getImage() != null) {
            try {
                book.setImage(bookRequest.getImage().getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to convert image file to byte array", e);
            }
        }

        return book;
    }

    // TODO cuidado
    public static BookRequest convertToBookRequest(Book book) {
        return BookRequest.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .year(book.getYear())
                .price(book.getPrice())
                .stock(book.getStock())
                .genre(book.getGenre())
                .description(book.getDescription())
                .build();
    }

}
