package com.bookstore.backend.jwt.config;

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
                        // Endpoints públicos y de error
                        .requestMatchers("/api/v1/auth/**").permitAll() // Registro, login, etc.
                        .requestMatchers("/error/**").permitAll() // Páginas de error
                        .requestMatchers("/public/**").permitAll() // Recursos públicos como imágenes, CSS, etc.
                        .requestMatchers("/books/**").permitAll() // Endpoints de categorías, solo accesibles por
                                                                  // usuarios autenticados
                        .requestMatchers("/images").permitAll()
                        .requestMatchers("/categories/**").hasAnyAuthority("USER", "ADMIN")
                        // Endpoints de carrito de compras, solo para usuarios con rol USER
                        .requestMatchers("/cart/**").hasAuthority("USER")
                        // Endpoint de historial de pedidos, solo accesible por usuarios con rol USER
                        .requestMatchers("/orders/history/**").hasAuthority("USER")
                        // Endpoints de administración, solo accesibles por usuarios con rol ADMIN
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/admin/products/**").hasAuthority("ADMIN")
                        .requestMatchers("/admin/users/**").hasAuthority("ADMIN")
                        // Endpoint de perfil, solo para el usuario autenticado
                        .requestMatchers("/profile/**").authenticated()
                        // Endpoint para finalizar compra, accesible solo por usuarios con rol USER
                        .requestMatchers("/checkout/**").hasAuthority("USER")
                        // API interna, solo accesible por usuarios con rol ADMIN
                        .requestMatchers("/api/internal/**").hasAuthority("ADMIN")
                        // Otros endpoints requieren autenticación
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}