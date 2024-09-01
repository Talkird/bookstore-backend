package com.bookstore.backend.service;

import java.util.List;

import com.bookstore.backend.exceptions.BookNotFoundException;
import com.bookstore.backend.exceptions.CartItemNotFoundException;
import com.bookstore.backend.exceptions.CartNotFoundException;
import com.bookstore.backend.exceptions.InvalidBookDataException;
import com.bookstore.backend.exceptions.UserNotFoundException;
import com.bookstore.backend.model.CartItem;

public interface CartService {

    public List<CartItem> getCart(Long userId) throws CartNotFoundException;
    
    public void clearCart(Long userId) throws UserNotFoundException, CartNotFoundException;

    public CartItem addItemToCart(Long userId, Long bookId) throws UserNotFoundException, BookNotFoundException, InvalidBookDataException;

    public CartItem updateCartItem(CartItem cartItem) throws CartItemNotFoundException;

    public void deleteCartItem(Long id) throws CartItemNotFoundException;

    public void checkoutCart(Long userId) throws UserNotFoundException, CartNotFoundException;
}