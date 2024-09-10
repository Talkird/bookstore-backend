package com.bookstore.backend.model.dto;

import org.springframework.beans.factory.annotation.Autowired;

import com.bookstore.backend.model.cart.CartItem;
import com.bookstore.backend.repository.BookRepository;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemRequest {
    private Long id;
    @JsonProperty("book_id")
    private Long bookId;
    private int quantity;

    @Autowired
    private static BookRepository bookRepository;

    public static CartItem convertToCartItem(CartItemRequest cartItemRequest) {
        CartItem cartItem = new CartItem();
        cartItem.setBook(bookRepository.findById(cartItemRequest.getBookId()).get());
        cartItem.setQuantity(cartItemRequest.getQuantity());
        return cartItem;

    }
}
