package com.bookstore.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.backend.model.Cart;
import com.bookstore.backend.service.CartService;

@RestController
public class CartController {
    
    @Autowired
    private CartService cartService;

    @GetMapping("/carts")
    public Page<Cart> getCart(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return cartService.getCart(pageRequest);
    }

    @DeleteMapping("/carts/{id}")
    public void clearCart() {
        cartService.clearCart();
    }

}
