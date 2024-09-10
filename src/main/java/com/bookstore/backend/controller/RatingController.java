package com.bookstore.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.backend.model.rating.Rating;
import com.bookstore.backend.service.rating.RatingService;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Rating>> getRatingsByBook(@PathVariable Long bookId) {
        List<Rating> ratings = ratingService.getRatingsByBook(bookId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Rating>> getRatingsByUser(@PathVariable Long userId) {
        List<Rating> ratings = ratingService.getRatingsByUser(userId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<Rating> getRatingByUserAndBook(@PathVariable Long userId, @PathVariable Long bookId) {
        Rating rating = ratingService.getRatingByUserAndBook(userId, bookId);
        return ResponseEntity.ok(rating);
    }

    @PostMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<Rating> createOrUpdateRating(@PathVariable Long userId,@PathVariable Long bookId,@RequestParam int ratingValue) {
        Rating rating = ratingService.updateOrCreateRating(userId, bookId, ratingValue);
        return ResponseEntity.ok(rating);
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long ratingId) {
        ratingService.deleteRating(ratingId);
        return ResponseEntity.noContent().build();
    }
}
