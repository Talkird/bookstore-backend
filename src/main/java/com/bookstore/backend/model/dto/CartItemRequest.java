package com.bookstore.backend.model.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long bookId;
    private int quantity;
}
