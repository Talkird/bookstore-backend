package com.bookstore.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.backend.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
