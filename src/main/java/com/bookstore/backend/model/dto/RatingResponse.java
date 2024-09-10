package com.bookstore.backend.model.dto;

import com.bookstore.backend.model.book.Book;
import com.bookstore.backend.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {

    private Long id;
    private Long userId;
    private Long bookId;
    private int rating;
}

