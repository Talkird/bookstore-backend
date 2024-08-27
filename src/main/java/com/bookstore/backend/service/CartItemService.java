package com.bookstore.backend.service;

import com.bookstore.backend.model.CartItem;

public interface CartItemService {
    public CartItem addCartItem(CartItem cartItem);

    public CartItem updateCartItem(CartItem cartItem);

    public void deleteCartItem(Long id);
}
