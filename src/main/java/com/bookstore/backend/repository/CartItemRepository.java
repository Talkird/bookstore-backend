package com.bookstore.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.backend.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    
}
