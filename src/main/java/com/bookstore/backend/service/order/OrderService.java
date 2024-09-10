package com.bookstore.backend.service.order;

import java.util.List;

import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.exception.order.OrderNotFoundException;
import com.bookstore.backend.model.dto.OrderResponse;
import com.bookstore.backend.model.order.Order;

public interface OrderService {
    List<OrderResponse> getOrdersByUserId(Long userId) throws UserNotFoundException;

    OrderResponse createOrder(Order order);

    OrderResponse getOrderById(Long orderId) throws OrderNotFoundException;

    void deleteOrder(Long orderId) throws OrderNotFoundException;

    OrderResponse updateOrder(Long orderId, Order updatedOrder) throws OrderNotFoundException;
}
