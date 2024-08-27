package com.bookstore.backend.model.dto;

import java.util.List;
import lombok.Data;

@Data
public class CartRequest {
    private Long id;
    private List<CartItemRequest> books;
    private UserRequest user;
    private double total;
}
