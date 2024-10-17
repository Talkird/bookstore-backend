package com.bookstore.backend.service.wishlist;

import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.exception.book.BookNotFoundException;
import com.bookstore.backend.model.wishlist.WishList;

import jakarta.persistence.EntityNotFoundException;

public interface WishListService {
    WishList getWishlistByUserId(Long userId) throws EntityNotFoundException;

    WishList addBookToWishlist(Long userId, Long bookId) throws UserNotFoundException, BookNotFoundException;

    WishList removeBookFromWishlist(Long userId, Long bookId) throws EntityNotFoundException;

    WishList createWishlistForUser(Long userId) throws UserNotFoundException;

}