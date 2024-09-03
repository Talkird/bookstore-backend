package com.bookstore.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.backend.model.book.Book;
import com.bookstore.backend.model.book.Genre;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByGenre(Genre genre);

    List<Book> findByPriceBetween(double minPrice, double maxPrice);

    List<Book> findByTitleContaining(String title);

    List<Book> findByAuthorContaining(String author);

    List<Book> findByStockGreaterThan(int stockThreshold);

    List<Book> findAllByOrderByPriceAsc();

    List<Book> findAllByOrderByPriceDesc();
}
