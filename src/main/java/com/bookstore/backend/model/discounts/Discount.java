package com.bookstore.backend.model.discounts;

import lombok.Data;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "discount")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private double percentage;

    @Column
    private boolean isActive; // Para activar o desactivar descuentos

    @Column
    private LocalDateTime expirationDate; // Fecha de expiración del código de descuento

    // Método para verificar si el descuento está activo y no expirado
    public boolean isValid() {
        return isActive && expirationDate.isAfter(LocalDateTime.now());
    }
}
