package com.bookstore.backend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ssl.SslProperties.Bundles.Watch.File;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.backend.model.book.Genre;
import com.bookstore.backend.model.dto.BookRequest;
import com.bookstore.backend.model.dto.BookResponse;
import com.bookstore.backend.service.book.BookService;

@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "http://localhost:5173")
public class BookController {

    @Autowired
    private BookService bookService;

    // ADMIN
    @GetMapping("/all")
    public ResponseEntity<List<BookResponse>> getBooks() {
        return ResponseEntity.ok(bookService.getBooks());
    }

    @PostMapping(value = "/create")
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest bookRequest) throws IOException {
        BookResponse createdBook = bookService.createBook(bookRequest);
        return ResponseEntity.status(201).body(createdBook);
    }

    // ADMIN
    @GetMapping("/get/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // ADMIN
    @PutMapping("/edit/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable Long id, @RequestBody BookRequest book) {
        BookResponse updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    // ADMIN
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully"); // 200 OK con mensaje personalizado
    }

    // ALL
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<BookResponse>> getBooksByGenre(@PathVariable("genre") String genreString) {
        Genre genre = Genre.fromString(genreString);
        return ResponseEntity.ok(bookService.getBookByGenre(genre));
    }

    // ALL
    @GetMapping("/price-range")
    public ResponseEntity<List<BookResponse>> getBooksByPriceRange(@RequestParam double minPrice,
            @RequestParam double maxPrice) {
        return ResponseEntity.ok(bookService.getBooksByPriceRange(minPrice, maxPrice));
    }

    // ALL
    @GetMapping("/title/{title}")
    public ResponseEntity<List<BookResponse>> getBooksByTitle(@PathVariable("title") String title) {
        return ResponseEntity.ok(bookService.getBooksByTitle(title));
    }

    // ALL
    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookResponse>> getBooksByAuthor(@PathVariable("author") String author) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(author));
    }

    // ALL
    @GetMapping("/available")
    public ResponseEntity<List<BookResponse>> getAvailableBooks() {
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }

    // ALL
    @GetMapping("/ordered-by-price-asc")
    public ResponseEntity<List<BookResponse>> getBooksOrderedByPriceAsc() {
        return ResponseEntity.ok(bookService.getBooksOrderedByPrice(true)); // true para orden ascendente
    }

    // ALL
    @GetMapping("/ordered-by-price-desc")
    public ResponseEntity<List<BookResponse>> getBooksOrderedByPriceDesc() {
        return ResponseEntity.ok(bookService.getBooksOrderedByPrice(false)); // false para orden descendente
    }

}