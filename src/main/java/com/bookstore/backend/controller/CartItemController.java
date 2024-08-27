package com.bookstore.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.backend.model.CartItem;
import com.bookstore.backend.service.CartItemService;

@RestController
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;
    
    @PostMapping("/carts/{id}")
    public CartItem addCartItem(CartItem cartItem) {
        return cartItemService.addCartItem(cartItem);
    }

    @PutMapping("/carts/{id}")
    public CartItem updateCartItem(CartItem cartItem) {
        return cartItemService.updateCartItem(cartItem);
    }

    @DeleteMapping("/carts/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
    }
}
