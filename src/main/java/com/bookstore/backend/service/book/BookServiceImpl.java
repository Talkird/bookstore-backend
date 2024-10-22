package com.bookstore.backend.service.book;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bookstore.backend.exception.book.BookAlreadyExistsException;
import com.bookstore.backend.exception.book.BookNotFoundException;
import com.bookstore.backend.exception.book.InvalidBookDataException;
import com.bookstore.backend.model.book.Book;
import com.bookstore.backend.model.book.Genre;
import com.bookstore.backend.model.dto.BookRequest;
import com.bookstore.backend.model.dto.BookResponse;
import com.bookstore.backend.repository.BookRepository;
import com.bookstore.backend.repository.CartItemRepository;
import com.bookstore.backend.repository.RatingRepository;

import jakarta.transaction.Transactional;

import java.io.IOException;
import java.io.File;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public List<BookResponse> getBooks() throws BookNotFoundException {
        List<BookResponse> books = BookResponse.convertToBookResponse(bookRepository.findAll());
        if (books.isEmpty()) {
            throw new BookNotFoundException("No se encontraron libros.");
        }
        return books;
    }

    @Override
    public BookResponse createBook(BookRequest book)
            throws IOException, BookAlreadyExistsException, InvalidBookDataException {
        String imagePath = book.getImagePath();

        Book newBook = BookRequest.convertToBook(book);
        newBook.setImagePath(imagePath);

        bookRepository.save(newBook);

        return BookResponse.convertToBookResponse(newBook);
    }

    @Override
    public BookResponse getBookById(Long id) throws BookNotFoundException {
        return BookResponse.convertToBookResponse(bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("No se encontró un libro con ID " + id)));
    }

    @Override
    public List<BookResponse> getBookByGenre(Genre genre) throws BookNotFoundException, InvalidBookDataException {
        return BookResponse.convertToBookResponse(bookRepository.findByGenre(genre)
                .orElseThrow(() -> new BookNotFoundException("No se encontraron libros para el género " + genre)));
    }

    @Override
    public BookResponse updateBook(Long id, BookRequest book) throws BookNotFoundException, InvalidBookDataException {
        Optional<Book> bookOptional = bookRepository.findById(id);

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

        return BookResponse.convertToBookResponse(bookRepository.save(existingBook));
    }

    @Transactional
    public void deleteBook(Long bookId) throws BookNotFoundException {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("No se encontró un libro con ID " + bookId);
        }

        cartItemRepository.deleteByBookId(bookId);
        ratingRepository.deleteByBookId(bookId);
        bookRepository.deleteById(bookId);
    }

    @Override
    public List<BookResponse> getBooksByPriceRange(double minPrice, double maxPrice) throws InvalidBookDataException {
        if (minPrice < 0 || maxPrice <= minPrice) {
            throw new InvalidBookDataException("Rango de precios invalido.");
        }
        return BookResponse.convertToBookResponse(bookRepository.findByPriceBetween(minPrice, maxPrice)
                .orElseThrow(() -> new BookNotFoundException(
                        "No se encontraron libros en el rango de precios especificado.")));
    }

    @Override
    public List<BookResponse> getBooksByTitle(String title) throws BookNotFoundException {
        return BookResponse.convertToBookResponse(bookRepository.findByTitleContaining(title)
                .orElseThrow(() -> new BookNotFoundException("No se encontraron libros con el título " + title)));
    }

    @Override
    public List<BookResponse> getBooksByAuthor(String author) throws BookNotFoundException {
        return BookResponse.convertToBookResponse(bookRepository.findByAuthorContaining(author)
                .orElseThrow(() -> new BookNotFoundException("No se encontraron libros del autor " + author)));
    }

    @Override
    public List<BookResponse> getAvailableBooks() throws BookNotFoundException {
        return BookResponse.convertToBookResponse(bookRepository.findByStockGreaterThan(0)
                .orElseThrow(() -> new BookNotFoundException("No se encontraron libros disponibles.")));
    }

    @Override
    public List<BookResponse> getBooksOrderedByPrice(boolean ascending) throws BookNotFoundException {
        Optional<List<Book>> books;
        if (ascending) {
            books = bookRepository.findAllByOrderByPriceAsc();
        } else {
            books = bookRepository.findAllByOrderByPriceDesc();
        }
        return BookResponse.convertToBookResponse(books
                .orElseThrow(() -> new BookNotFoundException("No se encontraron libros ordenados por precio.")));
    }

}
