package com.bookstore.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.backend.model.order.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
