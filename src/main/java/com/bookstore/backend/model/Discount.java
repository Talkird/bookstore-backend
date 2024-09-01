package com.bookstore.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@Table(name = "discount")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;  // Código del cupón

    @Column(nullable = false)
    private double percentage; // Porcentaje de descuento

    @Column(nullable = false)
    private boolean active; // Indicador de si el descuento está activo

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType; // Tipo de descuento (COUPON, TWO_BOOK_DISCOUNT, etc.)

    @Column(nullable = true)
    private LocalDate expirationDate; // Fecha de expiración solo relevante para cupones

    public Discount(String code, double percentage, boolean active, DiscountType discountType, LocalDate expirationDate) {
        this.code = code;
        this.percentage = percentage;
        this.active = active;
        this.discountType = discountType;
        this.expirationDate = expirationDate;
    }

    // Método para verificar si un cupón está caducado
    public boolean isExpired() {
        return expirationDate != null && LocalDate.now().isAfter(this.expirationDate);
    }

}
