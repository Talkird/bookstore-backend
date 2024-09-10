package com.bookstore.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.backend.model.dto.RatingRequest;
import com.bookstore.backend.model.dto.RatingResponse;
import com.bookstore.backend.service.rating.RatingService;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<RatingResponse>> getRatingsByBook(@PathVariable Long bookId) {
        List<RatingResponse> ratings = ratingService.getRatingsByBook(bookId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RatingResponse>> getRatingsByUser(@PathVariable Long userId) {
        List<RatingResponse> ratings = ratingService.getRatingsByUser(userId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<RatingResponse> getRatingByUserAndBook(@PathVariable Long userId, @PathVariable Long bookId) {
        RatingResponse rating = ratingService.getRatingByUserAndBook(userId, bookId);
        return ResponseEntity.ok(rating);
    }

    @PostMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<RatingResponse> createOrUpdateRating(@PathVariable Long userId,@PathVariable Long bookId,@RequestBody RatingRequest ratingRequest) {
        int ratingValue = ratingRequest.getRatingValue();
        RatingResponse rating = ratingService.updateOrCreateRating(userId, bookId, ratingValue);
        return ResponseEntity.ok(rating);
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long ratingId) {
        ratingService.deleteRating(ratingId);
        return ResponseEntity.noContent().build();
    }
}
