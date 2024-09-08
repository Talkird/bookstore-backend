package com.bookstore.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.backend.model.discounts.Discount;
import com.bookstore.backend.service.discount.DiscountService;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @PostMapping("/apply")
    public ResponseEntity<Double> applyDiscount(@RequestParam String discountCode, @RequestParam double totalPrice) {
        try {
            double newTotal = discountService.applyDiscount(discountCode, totalPrice);
            return ResponseEntity.ok(newTotal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Error en código de descuento
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Discount> createDiscount(@RequestBody Discount discount) {
        Discount newDiscount = discountService.createDiscount(discount);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDiscount);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Discount>> getActiveDiscounts() {
        List<Discount> discounts = discountService.getAllActiveDiscounts();
        return ResponseEntity.ok(discounts);
    }
}

