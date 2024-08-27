package com.bookstore.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.bookstore.backend.model.Book;

public interface BookService {
    public Page<Book> getBooks(PageRequest pageRequest);

    public Book createBook(Book book);

    public Book getBookById(Long id);

    public Book updateBook(Book book);

    public void deleteBook(Long id);
}
