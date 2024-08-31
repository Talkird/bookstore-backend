package com.bookstore.backend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.bookstore.backend.exceptions.BookAlreadyExistsException;
import com.bookstore.backend.exceptions.BookNotFoundException;
import com.bookstore.backend.exceptions.InvalidBookDataException;
import com.bookstore.backend.model.Book;
import com.bookstore.backend.model.Genre;

public interface BookService {

    public Page<Book> getBooks(PageRequest pageRequest) throws BookNotFoundException;

    public Book createBook(Book book) throws BookAlreadyExistsException, InvalidBookDataException;

    public Book getBookById(Long id) throws BookNotFoundException;

    public List<Book> getBookByGenre(Genre genre) throws BookNotFoundException, InvalidBookDataException;

    public Book updateBook(Book book) throws BookNotFoundException, InvalidBookDataException;

    public void deleteBook(Long id) throws BookNotFoundException;

    public List<Book> getBooksByPriceRange(double minPrice, double maxPrice) throws InvalidBookDataException;

    public List<Book> getBooksByTitle(String title) throws BookNotFoundException;

    public List<Book> getBooksByAuthor(String author) throws BookNotFoundException;

    public List<Book> getAvailableBooks() throws BookNotFoundException;

    public List<Book> getBooksOrderedByPrice(boolean ascending) throws BookNotFoundException, InvalidBookDataException;
}
