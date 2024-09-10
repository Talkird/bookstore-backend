package com.bookstore.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {

    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("book_id")
    private Long bookId;
    private int rating;
}

