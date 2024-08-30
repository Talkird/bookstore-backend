package com.bookstore.backend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookstore.backend.auth.AuthenticationRequest;
import com.bookstore.backend.auth.AuthenticationResponse;
import com.bookstore.backend.auth.RegisterRequest;
import com.bookstore.backend.config.JwtService;
import com.bookstore.backend.exception.EmailAlreadyExistsException;
import com.bookstore.backend.exception.InvalidCredentialsException;
import com.bookstore.backend.exception.UserNotFoundException;
import com.bookstore.backend.model.User;
import com.bookstore.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Error: Este correo electrónico ya está registrado.");
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
        } catch (Exception e) {
            throw new InvalidCredentialsException("Error: Email o contraseña incorrectos.");
        }

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Error: Usuario no encontrado con el email: " + request.getEmail()));

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }
}
