package com.bookstore.backend.service.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.exception.order.OrderNotFoundException;
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
    public List<Order> getOrdersByUserId(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con el id: " + userId));
        return user.getOrders();
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order getOrderById(Long orderId) throws OrderNotFoundException {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("Pedido no encontrado con el id: " + orderId));
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderNotFoundException {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException("No se encontró una orden con ID " + orderId);
        }
        orderRepository.deleteById(orderId);
    }

    @Override
    public Order updateOrder(Long orderId, Order updatedOrder) throws OrderNotFoundException {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("No se encontró una orden con ID " + orderId));

        existingOrder.setCustomerName(updatedOrder.getCustomerName());
        existingOrder.setCustomerEmail(updatedOrder.getCustomerEmail());
        existingOrder.setCustomerPhone(updatedOrder.getCustomerPhone());
        existingOrder.setShippingAddress(updatedOrder.getShippingAddress());
        existingOrder.setTotal(updatedOrder.getTotal());
        existingOrder.setPaymentMethod(updatedOrder.getPaymentMethod());
        existingOrder.setStatus(updatedOrder.getStatus());

        return orderRepository.save(existingOrder);
    }
}
