package com.bookstore.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.backend.model.CartItem;
import com.bookstore.backend.service.CartService;

@RestController
public class CartController {
    
    @Autowired
    private CartService cartService;

    @GetMapping("/carts/{id}")
    public List<CartItem> getCart(@PathVariable Long userId) {
        return cartService.getCart(userId);
    }

    @DeleteMapping("/carts/{id}")
    public void clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }

    @PostMapping("/carts/{id}")
    public CartItem addCartItem(@PathVariable Long userId, Long bookId) {
        return cartService.addItemToCart(userId, bookId);
    }

    @PutMapping("/carts")
    public CartItem updateCartItem(CartItem cartItem) {
        return cartService.updateCartItem(cartItem);
    }

    @DeleteMapping("/carts/item/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        cartService.deleteCartItem(id);
    }

}
