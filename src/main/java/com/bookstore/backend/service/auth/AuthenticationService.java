package com.bookstore.backend.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookstore.backend.exception.auth.EmailAlreadyExistsException;
import com.bookstore.backend.exception.auth.InvalidCredentialsException;
import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.jwt.auth.AuthenticationRequest;
import com.bookstore.backend.jwt.auth.AuthenticationResponse;
import com.bookstore.backend.jwt.auth.RegisterRequest;
import com.bookstore.backend.jwt.config.JwtService;
import com.bookstore.backend.model.user.User;
import com.bookstore.backend.repository.UserRepository;
import com.bookstore.backend.service.cart.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private CartService cartService;

    public AuthenticationResponse register(RegisterRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("El correo electrónico ya está registrado.");
        }

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        repository.save(user);
        cartService.createCart(user);
        var jwtToken = jwtService.generateToken(user);

        // Create and return the response including userId
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .userId(user.getId()) 
                .role(user.getRole().toString())
                .role(user.getEmail())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Nombre de usuario o contraseña incorrectos.");
        }

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(
                        () -> new UserNotFoundException("Usuario no encontrado con el correo: " + request.getEmail()));

        var jwtToken = jwtService.generateToken(user);

        // Create and return the response including userId
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .userId(user.getId()) // Assuming User has a getId() method
                .role(user.getRole().toString())
                .email(user.getEmail())
                .build();
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con el correo: " + email));
    }

    public User getUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con el id: " + id));
    }

    public void updateUser(User user) {
        repository.save(user);
    }
}