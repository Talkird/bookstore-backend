package com.bookstore.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.backend.model.cart.CartItem;
import com.bookstore.backend.model.dto.CartItemRequest;
import com.bookstore.backend.model.dto.OrderRequest;
import com.bookstore.backend.service.cart.CartService;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/carts/{userId}")
    public List<CartItemRequest> getCart(@PathVariable Long userId) {
        return cartService.getCart(userId);
    }

    @DeleteMapping("/carts/{userId}")
    public void clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }

    @PostMapping("/carts/{userId}")
    public CartItem addCartItem(@PathVariable Long userId, @RequestBody CartItemRequest cartItemRequest) {
        Long bookId = cartItemRequest.getBookId();
        int quantity = cartItemRequest.getQuantity();
        return cartService.addItemToCart(userId, bookId, quantity);
        }

    //VER QUE FUNCIONE
    @PutMapping("/carts/{userId}/item/{id}")
    public CartItem updateCartItem(@PathVariable Long userId,@PathVariable Long id,@RequestBody CartItemRequest cartItemRequest) {
        Long bookId = cartItemRequest.getBookId();
        int quantity = cartItemRequest.getQuantity();

        return cartService.updateCartItem(userId,id, bookId, quantity);
    }

    @DeleteMapping("/carts/{userId}/item/{id}")
    public void deleteCartItem(@PathVariable Long id, @PathVariable Long userId) {
        cartService.deleteCartItem(id,userId);
    }

    //VER QUE FUNCIONE
    @PostMapping("/carts/checkout/{userId}")
    public void checkoutCart(@PathVariable Long userId, @RequestBody OrderRequest orderRequest) {
        cartService.checkoutCart(userId, orderRequest.getCustomerName(), orderRequest.getCustomerEmail(), 
                                orderRequest.getCustomerPhone(), orderRequest.getShippingAddress(), 
                                orderRequest.getPaymentMethod(), orderRequest.getDiscountCode());
    }

}
