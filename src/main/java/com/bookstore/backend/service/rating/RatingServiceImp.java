package com.bookstore.backend.service.rating;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.exception.book.BookNotFoundException;
import com.bookstore.backend.exception.rating.RatingNotFoundException;
import com.bookstore.backend.model.book.Book;
import com.bookstore.backend.model.dto.BookRequest;
import com.bookstore.backend.model.dto.BookResponse;
import com.bookstore.backend.model.dto.RatingResponse;
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

    public RatingResponse updateOrCreateRating(Long userId, Long bookId, int ratingValue)
            throws UserNotFoundException, BookNotFoundException {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("Usuario no encontrado con ID: " + userId);
        }

        Book book = BookResponse.convertToBook(bookService.getBookById(bookId));
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
        bookService.updateBook(bookId, BookRequest.convertToBookRequest(book));

        return mapToRatingResponse(savedRating);
    }

    @Override
    public List<RatingResponse> getRatingsByBook(Long bookId) throws BookNotFoundException {
        List<Rating> ratings = ratingRepository.findByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException(
                        "No se encontraron calificaciones para el libro con ID: " + bookId));
        return mapToRatingResponse(ratings);
    }

    @Override
    public List<RatingResponse> getRatingsByUser(Long userId) throws UserNotFoundException {
        List<Rating> ratings = ratingRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "No se encontraron calificaciones para el usuario con ID: " + userId));
        return mapToRatingResponse(ratings);
    }

    @Override
    public RatingResponse getRatingByUserAndBook(Long userId, Long bookId) throws RatingNotFoundException {
        Rating rating = ratingRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RatingNotFoundException("Calificación no encontrada para el usuario con ID: "
                        + userId + " y el libro con ID: " + bookId));
        return mapToRatingResponse(rating);
    }

    @Override
    public void deleteRating(Long ratingId) throws RatingNotFoundException, BookNotFoundException {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RatingNotFoundException("Calificación no encontrada con ID: " + ratingId));

        Long bookId = rating.getBook().getId();
        ratingRepository.deleteById(ratingId);

        Book book = BookResponse.convertToBook(bookService.getBookById(bookId));
        book.getRatings().remove(rating);
        book.updateAverageRating();
        bookService.updateBook(bookId, BookRequest.convertToBookRequest(book));
    }

    private RatingResponse mapToRatingResponse(Rating rating) {
        return new RatingResponse(
                rating.getId(),
                rating.getUser().getId(),
                rating.getBook().getId(),
                rating.getRating());
    }

    private List<RatingResponse> mapToRatingResponse(List<Rating> ratings) {
        return ratings.stream().map(this::mapToRatingResponse).collect(Collectors.toList());
    }
}
