package com.bookstore.backend.model.dto;

import com.bookstore.backend.model.cart.CartItem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemRequest {
    private Long id;
    private BookRequest book;
    private int quantity;

    public static CartItem convertToCartItem(CartItemRequest cartItemRequest) {
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemRequest.getId());
        cartItem.setBook(BookRequest.convertToBook(cartItemRequest.getBook()));
        cartItem.setQuantity(cartItemRequest.getQuantity());
        return cartItem;

    }
}
