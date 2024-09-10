package com.bookstore.backend.service.rating;

import java.util.List;

import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.exception.book.BookNotFoundException;
import com.bookstore.backend.exception.rating.RatingNotFoundException;
import com.bookstore.backend.model.dto.RatingResponse;

public interface RatingService {
    RatingResponse updateOrCreateRating(Long userId, Long bookId, int ratingValue) throws UserNotFoundException, BookNotFoundException;

    List<RatingResponse> getRatingsByBook(Long bookId) throws BookNotFoundException;

    List<RatingResponse> getRatingsByUser(Long userId) throws UserNotFoundException;

    RatingResponse getRatingByUserAndBook(Long userId, Long bookId) throws RatingNotFoundException;

    void deleteRating(Long ratingId) throws RatingNotFoundException, BookNotFoundException;
}
