package com.bookstore.backend.model.dto;

import com.bookstore.backend.model.order.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OrderRequest {
    @JsonProperty("customer_name")
    private String customerName;
    @JsonProperty("customer_email")
    private String customerEmail;
    @JsonProperty("customer_phone")
    private String customerPhone;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;
    @JsonProperty("discount_code")
    private String discountCode;
}   