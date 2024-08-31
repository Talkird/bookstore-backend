package com.bookstore.backend.service;

import java.util.List;

import com.bookstore.backend.model.CartItem;

public interface CartService {

    public List<CartItem> getCart(Long userId);
    
    public void clearCart(Long userId);

    public CartItem addItemToCart(Long userId, Long bookId);

    public CartItem updateCartItem(CartItem cartItem);

    public void deleteCartItem(Long id);

    public void checkoutCart(Long userId);
}
