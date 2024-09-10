package com.bookstore.backend.model.dto;

import com.bookstore.backend.model.order.PaymentMethod;

import lombok.Data;

@Data
public class OrderRequest {
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String shippingAddress;
    private PaymentMethod paymentMethod;
    private String discountCode;
}   