package com.bookstore.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.backend.model.wishlist.WishList;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    Optional<WishList> findByUserId(Long userId);
}