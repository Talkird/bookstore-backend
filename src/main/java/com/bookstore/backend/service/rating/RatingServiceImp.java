package com.bookstore.backend.service.rating;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.exception.book.BookNotFoundException;
import com.bookstore.backend.exception.rating.RatingNotFoundException;
import com.bookstore.backend.model.book.Book;
import com.bookstore.backend.model.rating.Rating;
import com.bookstore.backend.model.user.User;
import com.bookstore.backend.repository.RatingRepository;
import com.bookstore.backend.service.auth.AuthenticationService;
import com.bookstore.backend.service.book.BookService;

@Service
public class RatingServiceImp implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthenticationService userService;

    @Override
    public Rating updateOrCreateRating(Long userId, Long bookId, int ratingValue) throws UserNotFoundException, BookNotFoundException {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("Usuario no encontrado con ID: " + userId);
        }

        Book book = bookService.getBookById(bookId);
        if (book == null) {
            throw new BookNotFoundException("Libro no encontrado con ID: " + bookId);
        }

        Optional<Rating> existingRating = ratingRepository.findByUserIdAndBookId(userId, bookId);
        Rating rating;

        if (existingRating.isPresent()) {
            rating = existingRating.get();
            book.getRatings().remove(rating);
            rating.setRating(ratingValue);

        } else {
            rating = new Rating();
            rating.setUser(user);
            rating.setBook(book);
            rating.setRating(ratingValue);
            user.getRatings().add(rating);

            userService.updateUser(user);
        }

        book.getRatings().add(rating);
        Rating savedRating = ratingRepository.save(rating);
        book.updateAverageRating();
        bookService.updateBook(book);

        return savedRating;
    }

    @Override
    public List<Rating> getRatingsByBook(Long bookId) throws BookNotFoundException {
        return ratingRepository.findByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException("No se encontraron calificaciones para el libro con ID: " + bookId));
    }

    @Override
    public List<Rating> getRatingsByUser(Long userId) throws UserNotFoundException {
        return ratingRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("No se encontraron calificaciones para el usuario con ID: " + userId));
    }

    @Override
    public Rating getRatingByUserAndBook(Long userId, Long bookId) throws RatingNotFoundException {
        return ratingRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RatingNotFoundException("Calificación no encontrada para el usuario con ID: " + userId + " y el libro con ID: " + bookId));
    }

    @Override
    public void deleteRating(Long ratingId) throws RatingNotFoundException, BookNotFoundException {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RatingNotFoundException("Calificación no encontrada con ID: " + ratingId));

        Long bookId = rating.getBook().getId();
        ratingRepository.deleteById(ratingId);

        Book book = bookService.getBookById(bookId);
        book.getRatings().remove(rating);
        book.updateAverageRating();
        bookService.updateBook(book);
    }
}
