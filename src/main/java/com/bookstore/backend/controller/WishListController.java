package com.bookstore.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.backend.model.wishlist.WishList;
import com.bookstore.backend.service.wishlist.WishListServiceImp;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishListController {

    private final WishListServiceImp wishlistService;

    @GetMapping("/wishlist/{userId}")
    public ResponseEntity<WishList> getWishlistByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(wishlistService.getWishlistByUserId(userId));
    }

    @PostMapping("/wishlist/{userId}/add/{bookId}")
    public ResponseEntity<WishList> addBookToWishlist(@PathVariable Long userId, @PathVariable Long bookId) {
        return ResponseEntity.ok(wishlistService.addBookToWishlist(userId, bookId));
    }

    @DeleteMapping("/wishlist/{userId}/remove/{bookId}")
    public ResponseEntity<WishList> removeBookFromWishlist(@PathVariable Long userId, @PathVariable Long bookId) {
        return ResponseEntity.ok(wishlistService.removeBookFromWishlist(userId, bookId));
    }

    @PostMapping("/wishlist/{userId}/create")
    public ResponseEntity<WishList> createWishlistForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(wishlistService.createWishlistForUser(userId));
    }
}