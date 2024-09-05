package com.bookstore.backend.service.rating;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Rating updateOrCreateRating(Long userId, Long bookId, int ratingValue) {
        User user = userService.getUserById(userId);
        Book book = bookService.getBookById(bookId);

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

            //userService.updateUser(user);//agregar metodo a user service
        }

        book.getRatings().add(rating);
        Rating savedRating = ratingRepository.save(rating);
        book.updateAverageRating();
        bookService.updateBook(book);

        return savedRating;
    }

    @Override
    public List<Rating> getRatingsByBook(Long bookId) {
        return ratingRepository.findByBookId(bookId).get();
    }

    @Override
    public List<Rating> getRatingsByUser(Long userId) {
        return ratingRepository.findByUserId(userId).get();
    }

    @Override
    public Rating getRatingByUserAndBook(Long userId, Long bookId) {
        return ratingRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
    }

    @Override
    public void deleteRating(Long ratingId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found"));

        Long bookId = rating.getBook().getId();
        ratingRepository.deleteById(ratingId);
 
        Book book = bookService.getBookById(bookId);
        book.getRatings().remove(rating);
        book.updateAverageRating();
        bookService.updateBook(book);
    }
}
