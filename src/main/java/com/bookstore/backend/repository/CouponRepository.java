package com.bookstore.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.backend.model.Coupon;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCodeAndActiveTrue(String code);
}
