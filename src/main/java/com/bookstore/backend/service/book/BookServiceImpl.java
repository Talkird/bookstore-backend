package com.bookstore.backend.service.book;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.backend.exception.book.BookAlreadyExistsException;
import com.bookstore.backend.exception.book.BookNotFoundException;
import com.bookstore.backend.exception.book.InvalidBookDataException;
import com.bookstore.backend.model.book.Book;
import com.bookstore.backend.model.book.Genre;
import com.bookstore.backend.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getBooks() throws BookNotFoundException {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            throw new BookNotFoundException("No se encontraron libros.");
        }
        return books;
    }

    @Override
    public Book createBook(Book book) throws BookAlreadyExistsException, InvalidBookDataException {
        if (book.getPrice() < 0) {
            throw new InvalidBookDataException("El precio debe ser positivo.");
        }

        return bookRepository.save(book);
    }

    @Override
    public Book getBookById(Long id) throws BookNotFoundException {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("No se encontró un libro con ID " + id));
    }

    @Override
    public List<Book> getBookByGenre(Genre genre) throws BookNotFoundException, InvalidBookDataException {
        List<Book> books = bookRepository.findByGenre(genre);
        if (books.isEmpty()) {
            throw new BookNotFoundException("No se encontraron libros para el género " + genre);
        }
        return books;
    }

    @Override
    public Book updateBook(Book book) throws BookNotFoundException, InvalidBookDataException {
        Optional<Book> bookOptional = bookRepository.findById(book.getId());

        if (!bookOptional.isPresent()) {
            throw new BookNotFoundException("No se encontró un libro con ID " + book.getId());
        }

        if (book.getPrice() < 0) {
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
    public void deleteBook(Long id) throws BookNotFoundException {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("No se encontró un libro con ID " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> getBooksByPriceRange(double minPrice, double maxPrice) throws InvalidBookDataException {
        if (minPrice < 0 || maxPrice <= minPrice) {
            throw new InvalidBookDataException("Rango de precios invalido.");
        }
        return bookRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public List<Book> getBooksByTitle(String title) throws BookNotFoundException {
        List<Book> books = bookRepository.findByTitleContaining(title);
        if (books.isEmpty()) {
            throw new BookNotFoundException("No se encontraron libros con el título " + title);
        }
        return books;
    }

    @Override
    public List<Book> getBooksByAuthor(String author) throws BookNotFoundException {
        List<Book> books = bookRepository.findByAuthorContaining(author);
        if (books.isEmpty()) {
            throw new BookNotFoundException("No se encontraron libros del autor " + author);
        }
        // Assuming author is always valid; otherwise, add more validation logic if
        // needed
        return books;
    }

    @Override
    public List<Book> getAvailableBooks() throws BookNotFoundException {
        List<Book> books = bookRepository.findByStockGreaterThan(0);
        if (books.isEmpty()) {
            throw new BookNotFoundException("No se encontraron libros disponibles.");
        }
        return books;
    }

    @Override
    public List<Book> getBooksOrderedByPrice(boolean ascending) throws BookNotFoundException {
        List<Book> books;
        if (ascending) {
            books = bookRepository.findAllByOrderByPriceAsc();
        } else {
            books = bookRepository.findAllByOrderByPriceDesc();
        }
        if (books.isEmpty()) {
            throw new BookNotFoundException("No se encontraron libros ordenados por precio.");
        }
        return books;
    }
}
