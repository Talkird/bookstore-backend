package com.bookstore.backend.service.order;

import java.util.List;

import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.exception.order.OrderNotFoundException;
import com.bookstore.backend.model.order.Order;

public interface OrderService {
    public List<Order> getOrdersByUserId(Long userId) throws UserNotFoundException;

    public Order createOrder(Order order);

    public Order getOrderById(Long orderId) throws OrderNotFoundException;

    public void deleteOrder(Long orderId) throws OrderNotFoundException;

    public Order updateOrder(Long orderId, Order updatedOrder) throws OrderNotFoundException;
}
