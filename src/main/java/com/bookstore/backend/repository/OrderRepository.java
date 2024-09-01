package com.bookstore.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bookstore.backend.model.Order;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserId(Long userId);

}
