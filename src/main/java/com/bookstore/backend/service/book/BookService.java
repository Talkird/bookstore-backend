package com.bookstore.backend.service.book;

import java.util.List;

import com.bookstore.backend.exception.book.BookAlreadyExistsException;
import com.bookstore.backend.exception.book.BookNotFoundException;
import com.bookstore.backend.exception.book.InvalidBookDataException;
import com.bookstore.backend.model.book.Genre;
import com.bookstore.backend.model.dto.BookRequest;
import com.bookstore.backend.model.dto.BookResponse;

public interface BookService {

    public List<BookResponse> getBooks() throws BookNotFoundException;

    public BookResponse createBook(BookRequest book) throws BookAlreadyExistsException, InvalidBookDataException;

    public BookResponse getBookById(Long id) throws BookNotFoundException;

    public List<BookResponse> getBookByGenre(Genre genre) throws BookNotFoundException, InvalidBookDataException;

    public BookResponse updateBook(BookRequest book) throws BookNotFoundException, InvalidBookDataException;

    public void deleteBook(Long id) throws BookNotFoundException;

    public List<BookResponse> getBooksByPriceRange(double minPrice, double maxPrice) throws InvalidBookDataException;

    public List<BookResponse> getBooksByTitle(String title) throws BookNotFoundException;

    public List<BookResponse> getBooksByAuthor(String author) throws BookNotFoundException;

    public List<BookResponse> getAvailableBooks() throws BookNotFoundException;

    public List<BookResponse> getBooksOrderedByPrice(boolean ascending) throws BookNotFoundException, InvalidBookDataException;
}