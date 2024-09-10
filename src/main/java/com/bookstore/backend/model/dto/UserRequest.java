package com.bookstore.backend.model.dto;

import com.bookstore.backend.model.user.Role;

import lombok.Data;

@Data
public class UserRequest {
    private Long id;
    private String email;
    private String name;
    private String password;
    private Role role;
}
