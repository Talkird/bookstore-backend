package com.bookstore.backend.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.bookstore.backend.model.Book;
import com.bookstore.backend.model.Genre;
import com.bookstore.backend.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Page<Book> getBooks(PageRequest pageRequest) {
        return bookRepository.findAll(pageRequest);
    }

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override 
    public List<Book> getBookByGenre(Genre genre) {
        return bookRepository.findByGenre(genre);
    }

    @Override
    public Book updateBook(Book book) {
        Optional<Book> bookOptional = bookRepository.findById(book.getId());

        if (!bookOptional.isPresent()) {
            return null;
        }

        Book existingBook = bookOptional.get();
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());
        existingBook.setStock(book.getStock());

        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> getBooksByPriceRange(double minPrice, double maxPrice) {
        return bookRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleContaining(title);
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContaining(author);
    }

    @Override
    public List<Book> getAvailableBooks() {
        return bookRepository.findByStockGreaterThan(0);
    }

    @Override
    public List<Book> getBooksOrderedByPrice(boolean ascending) {
        if (ascending) {
            return bookRepository.findAllByOrderByPriceAsc();
        } else {
            return bookRepository.findAllByOrderByPriceDesc();
        }
    }

    @Override
    public List<Book> getBooksOrderedByPublicationDate(boolean ascending) {
        if (ascending) {
            return bookRepository.findAllByOrderByPublicationDateAsc();
        } else {
            return bookRepository.findAllByOrderByPublicationDateDesc();
        }
    }
}
