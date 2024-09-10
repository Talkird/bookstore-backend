package com.bookstore.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.backend.model.dto.CartItemRequest;
import com.bookstore.backend.model.dto.CartItemResponse;
import com.bookstore.backend.model.dto.OrderRequest;
import com.bookstore.backend.service.cart.CartService;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/carts/{userId}")
    public ResponseEntity<List<CartItemResponse>> getCart(@PathVariable Long userId) {
        List<CartItemResponse> cartItems = cartService.getCart(userId);
        return ResponseEntity.ok(cartItems);
    }

    @DeleteMapping("/carts/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }

    @PostMapping("/carts/{userId}")
    public ResponseEntity<CartItemResponse> addCartItem(@PathVariable Long userId, @RequestBody CartItemRequest cartItemRequest) {
        CartItemResponse cartItem = cartService.addItemToCart(userId, cartItemRequest);
        return ResponseEntity.status(201).body(cartItem); // Devuelve 201 Created
    }

    @PutMapping("/carts/{userId}")
    public ResponseEntity<CartItemResponse> updateCartItem(@PathVariable Long userId, @RequestBody CartItemRequest cartItemRequest) {
        CartItemResponse updatedCartItem = cartService.updateCartItem(userId, cartItemRequest);
        return ResponseEntity.ok(updatedCartItem);
    }

    @DeleteMapping("/carts/{userId}/item/{cartItemId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable Long userId, @PathVariable Long cartItemId) {
        cartService.deleteCartItem(userId, cartItemId);
        return ResponseEntity.ok("Item removed from cart successfully");
    }

    @PostMapping("/carts/checkout/{userId}")
    public ResponseEntity<String> checkoutCart(@PathVariable Long userId, @RequestBody OrderRequest orderRequest) {
        cartService.checkoutCart(userId, orderRequest);
        return ResponseEntity.ok("Checkout completed successfully");
    }
}