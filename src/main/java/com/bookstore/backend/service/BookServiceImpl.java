package com.bookstore.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.bookstore.backend.model.Book;
import com.bookstore.backend.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public Page<Book> getBooks(PageRequest pageRequest) {
        return bookRepository.findAll(pageRequest);
    }

}
