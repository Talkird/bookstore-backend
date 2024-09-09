package com.bookstore.backend.model.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long id;
    private Long bookId;
    private String title;
    private String author;
    private int quantity;
    private double price;

    public CartItemRequest(Long bookId, String title, String author, int quantity, double price) {
        this.bookId= bookId;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.price = price;
    }
}
