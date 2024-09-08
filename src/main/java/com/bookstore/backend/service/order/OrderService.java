package com.bookstore.backend.service.order;

import java.util.List;

import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.exception.order.OrderNotFoundException;
import com.bookstore.backend.model.order.Order;

public interface OrderService {
    public List<Order> getOrdersByUserId(Long userId) throws UserNotFoundException;

    public Order createOrder(Order order);

    public Order getOrderById(Long orderId) throws OrderNotFoundException;
}
