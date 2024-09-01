package com.bookstore.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String customerPhone;

    @Column(nullable = false)
    private String shippingAddress;

    @Column(nullable = false)
    private double total;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private Cart cart;

    @Column(nullable = false)
    private OrderStatus status;
    
}
