package com.bookstore.backend.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.bookstore.backend.model.order.OrderItem;
import com.bookstore.backend.model.order.OrderStatus;
import com.bookstore.backend.model.order.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    @JsonProperty("customer_name")
    private String customerName;
    @JsonProperty("customer_email")
    private String customerEmail;
    @JsonProperty("customer_phone")
    private String customerPhone;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    private double total;
    private LocalDateTime date;
    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("cart_id")
    private Long cartId;
    @JsonProperty("items")
    private List<OrderItem> items;

    private OrderStatus status;
}
