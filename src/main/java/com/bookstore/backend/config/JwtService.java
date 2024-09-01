package com.bookstore.backend.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.bookstore.backend.exceptions.JwtTokenMalformedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${application.security.jwt.secretKey}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(UserDetails userDetails) {
        System.out.println("Clave secreta utilizada: " + secretKey);
        System.out.println("Tiempo de expiración: " + jwtExpiration);
        return buildToken(userDetails, jwtExpiration);
    }

    private String buildToken(UserDetails userDetails, long expiration) {
        String email = userDetails.getUsername();
        System.out.println("Generando token para el email: " + email); 
        return Jwts
                .builder()
                .subject(email) 
                .issuedAt(new Date(System.currentTimeMillis())) 
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey())
                .compact();
    }
    

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
                final String username = extractClaim(token, Claims::getSubject);
                return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
            } catch (JwtException | IllegalArgumentException e) {
                throw new JwtTokenMalformedException("El token JWT no es válido.");
            }    
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        SecretKey secretKeySpec = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return secretKeySpec;
    }
}
