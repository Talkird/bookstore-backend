package com.bookstore.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.backend.model.book.Book;
import com.bookstore.backend.model.book.Genre;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<List<Book>> findByGenre(Genre genre);

    Optional<List<Book>> findByPriceBetween(double minPrice, double maxPrice);

    Optional<List<Book>> findByTitleContaining(String title);

    Optional<List<Book>> findByAuthorContaining(String author);

    Optional<List<Book>> findByStockGreaterThan(int stockThreshold);

    Optional<List<Book>> findAllByOrderByPriceAsc();

    Optional<List<Book>> findAllByOrderByPriceDesc();
}
