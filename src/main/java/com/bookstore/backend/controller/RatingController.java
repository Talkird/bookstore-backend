package com.bookstore.backend.controller;

import com.bookstore.backend.model.dto.RatingRequest;
import com.bookstore.backend.model.rating.Rating;
import com.bookstore.backend.service.rating.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
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
    public ResponseEntity<Rating> createOrUpdateRating(@PathVariable Long userId,@PathVariable Long bookId,@RequestBody RatingRequest ratingRequest) {
        int ratingValue = ratingRequest.getRatingValue();
        Rating rating = ratingService.updateOrCreateRating(userId, bookId, ratingValue);
        return ResponseEntity.ok(rating);
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<String> deleteRating(@PathVariable Long ratingId) {
        ratingService.deleteRating(ratingId);
        return ResponseEntity.ok("Se eliminó correctamente");
    }
}
