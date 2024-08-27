package com.bookstore.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.bookstore.backend.model.Cart;

public interface CartService {
    public Page<Cart> getCart(PageRequest pageRequest);

    public void clearCart();


}
