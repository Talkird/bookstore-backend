package com.bookstore.backend.service.rating;

import java.util.List;

import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.exception.book.BookNotFoundException;
import com.bookstore.backend.exception.rating.RatingNotFoundException;
import com.bookstore.backend.model.rating.Rating;

public interface RatingService {
    Rating updateOrCreateRating(Long userId, Long bookId, int ratingValue) throws UserNotFoundException, BookNotFoundException;

    List<Rating> getRatingsByBook(Long bookId) throws BookNotFoundException;

    List<Rating> getRatingsByUser(Long userId) throws UserNotFoundException;

    Rating getRatingByUserAndBook(Long userId, Long bookId) throws RatingNotFoundException;

    void deleteRating(Long ratingId) throws RatingNotFoundException, BookNotFoundException;
}
