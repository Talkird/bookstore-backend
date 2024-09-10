package com.bookstore.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RatingRequest {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("book_id")
    private Long bookId;
    @JsonProperty("rating_value")
    private int ratingValue;
}
