package com.bookstore.backend.jwt.auth;   

import com.bookstore.backend.model.user.Role;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank 
    private String username;  

    @Email 
    private String email;

    @NotBlank
    @Size(min=6)
    private String password;

    private Role role;
}
