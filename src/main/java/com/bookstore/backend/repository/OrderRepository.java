package com.bookstore.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.backend.model.order.Order;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserId(Long userId);

}
