package com.bookstore.backend.service.order;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.backend.exception.auth.UserNotFoundException;
import com.bookstore.backend.exception.order.OrderNotFoundException;
import com.bookstore.backend.model.dto.OrderResponse;
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
    public List<OrderResponse> getOrdersByUserId(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con el id: " + userId));
        return mapToOrderResponse(user.getOrders());
    }

    @Override
    public OrderResponse createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        return mapToOrderResponse(savedOrder);
    }

    public OrderResponse getOrderById(Long orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Pedido no encontrado con el id: " + orderId));
        return mapToOrderResponse(order);
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderNotFoundException {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException("No se encontró una orden con ID " + orderId);
        }
        orderRepository.deleteById(orderId);
    }

    @Override
    public OrderResponse updateOrder(Long orderId, Order updatedOrder) throws OrderNotFoundException {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("No se encontró una orden con ID " + orderId));

        existingOrder.setCustomerName(updatedOrder.getCustomerName());
        existingOrder.setCustomerEmail(updatedOrder.getCustomerEmail());
        existingOrder.setCustomerPhone(updatedOrder.getCustomerPhone());
        existingOrder.setShippingAddress(updatedOrder.getShippingAddress());
        existingOrder.setTotal(updatedOrder.getTotal());
        existingOrder.setPaymentMethod(updatedOrder.getPaymentMethod());
        existingOrder.setCart(updatedOrder.getCart());
        existingOrder.setStatus(updatedOrder.getStatus());

        Order savedOrder = orderRepository.save(existingOrder);
        return mapToOrderResponse(savedOrder);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getCustomerPhone(),
                order.getShippingAddress(),
                order.getTotal(),
                order.getDate(),
                order.getPaymentMethod(),
                order.getUser().getId(),
                order.getCart().getId(),
                order.getCart().getBooks(), // List<CartItem>
                order.getStatus());
    }

    private List<OrderResponse> mapToOrderResponse(List<Order> orders) {
        return orders.stream().map(this::mapToOrderResponse).collect(Collectors.toList());
    }

}
