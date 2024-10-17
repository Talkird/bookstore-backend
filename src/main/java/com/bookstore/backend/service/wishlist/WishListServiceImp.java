package com.bookstore.backend.service.wishlist;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.exception.book.BookNotFoundException;
import com.bookstore.backend.model.book.Book;
import com.bookstore.backend.model.user.User;
import com.bookstore.backend.repository.WishListRepository;
import com.bookstore.backend.model.wishlist.WishList;
import com.bookstore.backend.repository.BookRepository;
import com.bookstore.backend.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishListServiceImp {

    private final WishListRepository wishlistRepository;
    private final UserRepository userRepository;
private final BookRepository bookRepository;

public WishList getWishlistByUserId(Long userId) {
        return wishlistRepository.findByUserId(userId)
                        .orElseThrow(() -> new EntityNotFoundException("No se encontro lista de deseados para el usuario con id: " + userId));
}

public WishList addBookToWishlist(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException("No se encontro lista de deseados para el usuario con id: " + userId));

        Book book = bookRepository.findById(bookId)
                        .orElseThrow(() -> new BookNotFoundException("No se encontro libro con id: " + bookId));

        WishList wishlist = wishlistRepository.findByUserId(userId)
                        .orElseThrow(() -> new EntityNotFoundException("No se encontro lista de deseados para el usuario con id: " + userId));

        wishlist.getBooks().add(book);
        return wishlistRepository.save(wishlist);
    }

    public WishList removeBookFromWishlist(Long userId, Long bookId) {
        WishList wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro lista de deseados para el usuario con id: " + userId));

        wishlist.getBooks().removeIf(book -> book.getId().equals(bookId));
        return wishlistRepository.save(wishlist);
    }

    public WishList createWishlistForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No se encontro lista de deseados para el usuario con id: " + userId));

        WishList wishlist = WishList.builder()
                .user(user)
                .books(List.of())
                .build();

        return wishlistRepository.save(wishlist);
    }
}