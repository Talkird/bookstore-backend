package com.bookstore.backend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.bookstore.backend.model.Book;
import com.bookstore.backend.model.Genre;

public interface BookService {
    public Page<Book> getBooks(PageRequest pageRequest);

    public Book createBook(Book book);

    public Book getBookById(Long id);

    public List<Book> getBookByGenre(Genre genre);

    public Book updateBook(Book book);

    public void deleteBook(Long id);

}
