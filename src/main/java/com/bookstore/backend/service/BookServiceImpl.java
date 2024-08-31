package com.bookstore.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.bookstore.backend.exceptions.BookAlreadyExistsException;
import com.bookstore.backend.exceptions.BookNotFoundException;
import com.bookstore.backend.exceptions.InvalidBookDataException;
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
        if (bookRepository.existsById(book.getId())) {
            throw new BookAlreadyExistsException("Un libro con ISBN " + book.getIsbn() + " ya existe.");
        }
        
        if (book.getPrice() <= 0) {
            throw new InvalidBookDataException("El precio debe ser positivo.");
        }

        return bookRepository.save(book);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> 
            new BookNotFoundException("No se encontró un libro con ID " + id));
    }

    @Override
    public List<Book> getBookByGenre(Genre genre) {
        return bookRepository.findByGenre(genre);
    }

    @Override
    public Book updateBook(Book book) {
        Optional<Book> bookOptional = bookRepository.findById(book.getId());

        if (!bookOptional.isPresent()) {
            throw new BookNotFoundException("No se encontró un libro con ID " + book.getId());
        }

        if (book.getPrice() <= 0) {
            throw new InvalidBookDataException("El precio debe ser positivo.");
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
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("No se encontró un libro con ID " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> getBooksByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0) {
            throw new InvalidBookDataException("El rango de precios debe ser positivo.");
        }
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

}
