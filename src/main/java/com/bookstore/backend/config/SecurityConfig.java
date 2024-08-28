package com.bookstore.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req -> req
                // Endpoints que no requieren autenticación
                .requestMatchers("/api/v1/auth/**").permitAll() // Registro, login, etc.
                .requestMatchers("/error/**").permitAll() // Páginas de error
                .requestMatchers("/public/**").permitAll() // Recursos públicos como imágenes, CSS, etc.
                // Endpoints de categorías, solo accesibles por usuarios autenticados
                .requestMatchers("/categories/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                // Endpoints de carrito de compras, solo para usuarios con rol USER
                .requestMatchers("/cart/**").hasAuthority(Role.USER.name())
                // Endpoints de administración, solo accesibles por usuarios con rol ADMIN
                .requestMatchers("/admin/**").hasAuthority(Role.ADMIN.name())
                // Endpoint de perfil, solo para el usuario autenticado
                .requestMatchers("/profile/**").authenticated()
                // Endpoint para finalizar compra, accesible solo por usuarios con rol USER
                .requestMatchers("/checkout/**").hasAuthority(Role.USER.name())
                // Otros endpoints requieren autenticación
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}