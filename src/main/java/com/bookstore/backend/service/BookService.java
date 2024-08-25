package com.bookstore.backend.service;

import java.util.List;

import com.bookstore.backend.model.Book;
import com.bookstore.backend.repository.BookRepository;

public class BookService {
        private final BookRepository bookRepository;
    
        public BookService(BookRepository bookRepository) {
            this.bookRepository = bookRepository;
        }
    
        public List<Book> getAllBooks() {
            return bookRepository.findAll();
        }
    
        public Book getBookById(Long id) {
            return bookRepository.findById(id).orElse(null);
        }
    
        public Book createBook(Book book) {
            return bookRepository.save(book);
        }
    
        public Book updateBook(Long id, Book book) {
            Book existingBook = bookRepository.findById(id).orElse(null);
            if (existingBook != null) {
                existingBook.setTitle(book.getTitle());
                existingBook.setAuthor(book.getAuthor());
                existingBook.setGenre(book.getGenre());
                existingBook.setYear(book.getYear());
                existingBook.setPrice(book.getPrice());
                return bookRepository.save(existingBook);
            } else {
                return null;
            }
        }
    
        public boolean deleteBook(Long id) {
            Book existingBook = bookRepository.findById(id).orElse(null);
            if (existingBook != null) {
                bookRepository.delete(existingBook);
                return true;
            } else {
                return false;
            }
        }
}
