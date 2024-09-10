package com.bookstore.backend.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.bookstore.backend.model.cart.CartItem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {
    private Long id;
    private BookResponse book;
    private int quantity;
    private double price;

    public static CartItemResponse convertToCartItemResponse(CartItem cartItem) {
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .book(BookResponse.convertToBookResponse(cartItem.getBook()))
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .build();

    }

    public static List<CartItemResponse> convertToCartItemResponse(List<CartItem> cartItems) {
        List<CartItemResponse> cartItemResponses = new ArrayList<>();
        cartItems.forEach(cartItem -> cartItemResponses.add(convertToCartItemResponse(cartItem)));
        return cartItemResponses;
    }
}
