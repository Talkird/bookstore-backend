package com.bookstore.backend.model.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long id;
    private CartRequest cart;
    private BookRequest book;
    private int quantity;
    private double price;
}
