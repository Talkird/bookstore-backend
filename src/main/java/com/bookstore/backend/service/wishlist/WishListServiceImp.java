package com.bookstore.backend.service.wishlist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
public class WishListServiceImp implements WishListService {
    
    @Autowired
    private final WishListRepository wishlistRepository;
    
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BookRepository bookRepository;

    @Override
    public WishList getWishlistByUserId(Long userId) throws EntityNotFoundException {
            return wishlistRepository.findByUserId(userId)
                            .orElseThrow(() -> new EntityNotFoundException("No se encontro lista de deseados para el usuario con id: " + userId));
    }

    @Override
    public WishList addBookToWishlist(Long userId, Long bookId) throws UserNotFoundException, BookNotFoundException {
            User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UserNotFoundException("No se encontro lista de deseados para el usuario con id: " + userId));

            Book book = bookRepository.findById(bookId)
                            .orElseThrow(() -> new BookNotFoundException("No se encontro libro con id: " + bookId));

            WishList wishlist = wishlistRepository.findByUserId(userId)
                            .orElseThrow(() -> new EntityNotFoundException("No se encontro lista de deseados para el usuario con id: " + userId));

            wishlist.getBooks().add(book);
            return wishlistRepository.save(wishlist);
    }

    @Override
    public WishList removeBookFromWishlist(Long userId, Long bookId) throws EntityNotFoundException {
        WishList wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro lista de deseados para el usuario con id: " + userId));

        wishlist.getBooks().removeIf(book -> book.getId().equals(bookId));
        return wishlistRepository.save(wishlist);
    }

    @Override
    public WishList createWishlistForUser(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No se encontro lista de deseados para el usuario con id: " + userId));

        WishList wishlist = WishList.builder()
                .user(user)
                .books(List.of())
                .build();

        return wishlistRepository.save(wishlist);
    }
}