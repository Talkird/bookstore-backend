package com.bookstore.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.backend.model.cart.CartItem;
import com.bookstore.backend.model.order.PaymentMethod;
import com.bookstore.backend.service.cart.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{id}")
    public List<CartItem> getCart(@PathVariable Long userId) {
        return cartService.getCart(userId);
    }

    @DeleteMapping("/delete/{id}")
    public void clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }

    @PostMapping("/add/{id}")
    public CartItem addCartItem(@PathVariable Long userId, Long bookId) {
        return cartService.addItemToCart(userId, bookId);
    }

    @PutMapping("/edit")
    public CartItem updateCartItem(@RequestBody CartItem cartItem) {
        return cartService.updateCartItem(cartItem);
    }

    @DeleteMapping("/delete/item/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        cartService.deleteCartItem(id);
    }

    @PostMapping("/edit/checkout/{id}")
    public void checkoutCart(@PathVariable Long userId, String customerName, String customerEmail, 
    String customerPhone, String shippingAdress, PaymentMethod paymentMethod) {
        cartService.checkoutCart(userId, customerName, customerEmail, customerPhone, shippingAdress, paymentMethod);
    }

}
