package com.bookstore.backend.service.rating;

import java.util.List;

import com.bookstore.backend.model.rating.Rating;

public interface RatingService {

    public Rating updateOrCreateRating(Long userId, Long bookId, int ratingValue);

    public List<Rating> getRatingsByBook(Long bookId);

    public List<Rating> getRatingsByUser(Long userId);

    public Rating getRatingByUserAndBook(Long userId, Long bookId);

    public void deleteRating(Long ratingId);
}
