package com.bookstore.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.bookstore.backend.model.Cart;
import com.bookstore.backend.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Override
    public Page<Cart> getCart(PageRequest pageRequest) {
        //TODO
        return null;
    }

    @Override
    public void clearCart() {
        //TODO;
    }
    
}
