package com.bookstore.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.backend.model.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Discount findByCode(String discountCode);
    Optional<Discount> findByCodeAndActiveTrue(String code);
}
