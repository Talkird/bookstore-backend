package com.bookstore.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.backend.model.discounts.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Discount findByCode(String discountCode);
}
