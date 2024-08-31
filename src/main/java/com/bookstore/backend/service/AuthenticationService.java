package com.bookstore.backend.service; 

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookstore.backend.auth.AuthenticationRequest;
import com.bookstore.backend.auth.AuthenticationResponse;
import com.bookstore.backend.auth.RegisterRequest;
import com.bookstore.backend.config.JwtService;
import com.bookstore.backend.exceptions.EmailAlreadyExistsException;
import com.bookstore.backend.exceptions.InvalidCredentialsException;
import com.bookstore.backend.exceptions.UserNotFoundException;
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
                        if (repository.existsByEmail(request.getEmail())) {
                throw new EmailAlreadyExistsException("El correo electrónico ya está registrado.");
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
                    // Intentar autenticar al usuario con las credenciales proporcionadas
                    authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                        )
                    );
                } catch (BadCredentialsException ex) {
                    // Lanzar excepción personalizada si las credenciales son incorrectas
                    throw new InvalidCredentialsException("Nombre de usuario o contraseña incorrectos.");
                }
            
                var user = repository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con el correo: " + request.getEmail()));
            
                var jwtToken = jwtService.generateToken(user);
            
                return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .build();
            }

}