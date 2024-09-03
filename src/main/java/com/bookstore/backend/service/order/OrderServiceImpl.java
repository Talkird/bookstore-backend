package com.bookstore.backend.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

import com.bookstore.backend.model.order.Order;
import com.bookstore.backend.model.user.User;
import com.bookstore.backend.repository.OrderRepository;
import com.bookstore.backend.repository.UserRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        // TODO implementar exception para este metodo
        Optional<User> user = userRepository.findById(userId);
        return user.get().getOrders();
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

}
