package com.bookstore.backend.repository;

import com.bookstore.backend.model.wishlist.WishList;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    Optional<WishList> findByUserId(Long userId);

    Optional<WishList> findByUserIdAndBookId(Long userId, Long bookId);
}