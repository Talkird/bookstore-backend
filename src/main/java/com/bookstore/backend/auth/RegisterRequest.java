package com.bookstore.backend.auth;   //todo

import com.bookstore.model.Role; //todo

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;  //todo 
    private String lastname;    //eliminar?
    private String email;
    private String password;
    private Role role;
}
