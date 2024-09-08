package com.bookstore.backend.dto.cart;

import com.bookstore.backend.dto.book.BookRequest;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long id;
    private CartRequest cart;
    private BookRequest book;
    private int quantity;
    private double price;
}
