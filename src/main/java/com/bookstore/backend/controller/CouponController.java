package com.bookstore.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bookstore.backend.model.discounts.Coupon;
import com.bookstore.backend.repository.CouponRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    @Autowired
    private CouponRepository couponRepository;

    @PostMapping("/apply")
    public ResponseEntity<?> applyCoupon(@RequestParam String code, @RequestParam Double originalPrice) {
        Optional<Coupon> couponOptional = couponRepository.findByCodeAndActiveTrue(code);
        if (couponOptional.isPresent()) {
            Coupon coupon = couponOptional.get();
            Double discountPrice = originalPrice;

            discountPrice = originalPrice - (originalPrice * coupon.getPercentage());
            return ResponseEntity.ok("Discounted price: " + discountPrice);
        } else {
            return ResponseEntity.badRequest().body("Invalid or inactive coupon code");
        }
    }
}

