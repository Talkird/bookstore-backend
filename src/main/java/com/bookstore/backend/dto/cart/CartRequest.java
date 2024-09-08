package com.bookstore.backend.dto.cart;

import java.util.List;

import com.bookstore.backend.dto.auth.UserRequest;

import lombok.Data;

@Data
public class CartRequest {
    private Long id;
    private List<CartItemRequest> books;
    private UserRequest user;
    private double total;
}
