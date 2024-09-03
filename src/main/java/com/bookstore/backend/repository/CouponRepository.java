package com.bookstore.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.backend.model.discounts.Coupon;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCodeAndActiveTrue(String code);
}
