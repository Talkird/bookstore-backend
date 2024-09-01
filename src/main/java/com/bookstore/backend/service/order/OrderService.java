package com.bookstore.backend.service.order;

import java.util.List;
import com.bookstore.backend.model.Order;

public interface OrderService {
    // get userId from Order object cart
    public List<Order> getOrdersByUserId(Long userId); // TODO agregar throws exceptions

    public Order createOrder(Order order);
}
