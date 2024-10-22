package com.bookstore.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.backend.model.rating.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<List<Rating>> findByUserId(Long userId);

    Optional<List<Rating>> findByBookId(Long bookId);

    Optional<Rating> findByUserIdAndBookId(Long userId, Long bookId);

    void deleteByBookId(Long bookId);
}
