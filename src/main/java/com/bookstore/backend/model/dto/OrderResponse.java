package com.bookstore.backend.model.dto;

import java.time.LocalDateTime;

import com.bookstore.backend.model.order.OrderStatus;
import com.bookstore.backend.model.order.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String shippingAddress;
    private double total;
    private LocalDateTime date;
    private PaymentMethod paymentMethod;
    private Long user;
    private Long cart;
    private OrderStatus status;
}

