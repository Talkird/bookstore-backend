package com.bookstore.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "cart")
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> books;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private double total;

    @ManyToOne // Relación con Discount para aplicar un descuento al carrito
    @JoinColumn(name = "discount_id")
    private Discount discount;

    public void updateTotal() {
        this.total = 0;
        for (CartItem cartItem : books) {
            total += cartItem.getPrice() * cartItem.getQuantity();
        }

        // Aplicar el descuento si existe y está activo
        if (discount != null && discount.isActive()) {
            if (discount.getDiscountType() == DiscountType.TWO_BOOK_DISCOUNT) {
                applyTwoBookDiscount();
            } else if (discount.getDiscountType() == DiscountType.COUPON && !discount.isExpired()) {
                total -= total * (discount.getPercentage() / 100);
            }
            // Otros tipos de descuentos pueden ser añadidos aquí
        }
    }

    private void applyTwoBookDiscount() {
        // Lógica para aplicar el descuento del 35% al segundo libro o al más caro
    }
}