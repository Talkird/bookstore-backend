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
                .requestMatchers("/api/v1/auth/**").permitAll() //cualquier usuario podrá acceder a él sin necesidad de estar autenticado. Esto es útil para endpoints como el login o el registro, donde no tiene sentido pedir autenticación previa.
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN") //solo los usuarios que tengan el rol ADMIN podrán acceder a él. Los usuarios con otros roles, o sin autenticación, recibirán un error de acceso denegado.
                .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN") // los usuarios con el rol USER como los que tienen el rol ADMIN podrán acceder. Esto permite que tanto usuarios comunes como administradores puedan acceder a estos recursos.
                .anyRequest().authenticated() //Esta línea asegura que cualquier otra solicitud no especificada anteriormente requiere que el usuario esté autenticado, sin importar el rol.
            )
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
