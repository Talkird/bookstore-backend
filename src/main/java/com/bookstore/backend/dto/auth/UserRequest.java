package com.bookstore.backend.dto.auth;

import com.bookstore.backend.model.user.Role;

import lombok.Data;

@Data
public class UserRequest {
    private Long id;
    private String email;
    private String username;
    private String password;
    private Role role;
}
