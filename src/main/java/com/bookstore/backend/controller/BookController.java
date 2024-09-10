package com.bookstore.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.backend.model.book.Book;
import com.bookstore.backend.model.book.Genre;
import com.bookstore.backend.service.book.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    //ADMIN
    @GetMapping("/all")
    public List<Book> getBooks() {
        return bookService.getBooks();
    }

    //ADMIN
    @PostMapping("/create")
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    //ADMIN
    @GetMapping("/get/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    //ADMIN
    @PutMapping("/edit/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(book);
    }

    //ADMIN
    @DeleteMapping("/delete/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    //ALL 
    @GetMapping("/genre/{genre}")
    public List<Book> getBooksByGenre(@PathVariable("genre") String genreString) {
        Genre genre = Genre.fromString(genreString);
        return bookService.getBookByGenre(genre);
    }

    //ALL ver
    @GetMapping("/price-range")
    public List<Book> getBooksByPriceRange(@RequestParam double minPrice, @RequestParam double maxPrice) {
        return bookService.getBooksByPriceRange(minPrice, maxPrice);
    }

    //ALL 
    @GetMapping("/title/{title}")
    public List<Book> getBooksByTitle(@PathVariable("title") String title) {
        return bookService.getBooksByTitle(title);
    }

    //ALL 
    @GetMapping("/author/{author}")
    public List<Book> getBooksByAuthor(@PathVariable("author") String author) {
        return bookService.getBooksByAuthor(author);
    }

    //ALL
    //Obtener libros disponibles cuyo stock >0
    @GetMapping("/available")
    public List<Book> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }

    //ALL
    @GetMapping("/ordered-by-price-asc")
    public List<Book> getBooksOrderedByPriceAsc() {
        return bookService.getBooksOrderedByPrice(true);  // true para orden ascendente
    }

    //ALL
    @GetMapping("/ordered-by-price-desc")
    public List<Book> getBooksOrderedByPriceDesc() {
        return bookService.getBooksOrderedByPrice(false);  // false para orden descendente
    }


}
