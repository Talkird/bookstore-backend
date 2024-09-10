package com.bookstore.backend.model.dto;

import lombok.Data;

@Data
public class RatingRequest {
    private Long userId;
    private Long bookId;
    private int ratingValue;
}
