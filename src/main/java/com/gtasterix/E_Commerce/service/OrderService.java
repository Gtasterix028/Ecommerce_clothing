package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.dto.OrderDTO;
import com.gtasterix.E_Commerce.exception.OrderNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.mapper.OrderMapper;
import com.gtasterix.E_Commerce.model.Order;
import com.gtasterix.E_Commerce.model.OrderStatus;
import com.gtasterix.E_Commerce.model.User;
import com.gtasterix.E_Commerce.repository.OrderRepository;
import com.gtasterix.E_Commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public OrderDTO createOrder(OrderDTO orderDTO) {
        validateOrderDTO(orderDTO);
        User user = userRepository.findById(orderDTO.getUserID())
                .orElseThrow(() -> new ValidationException("User with ID " + orderDTO.getUserID() + " not found"));
        Order order = OrderMapper.toEntity(orderDTO, user);
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.toDTO(savedOrder);
    }

    public OrderDTO getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
        return OrderMapper.toDTO(order);
    }

    public OrderDTO updateOrderById(UUID orderId, OrderDTO orderDTO) {
        validateOrderDTO(orderDTO);
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException("Order not found with ID: " + orderId);
        }
        User user = userRepository.findById(orderDTO.getUserID())
                .orElseThrow(() -> new ValidationException("User with ID " + orderDTO.getUserID() + " not found"));
        Order order = OrderMapper.toEntity(orderDTO, user);
        order.setOrderID(orderId); // Ensure ID is not modified
        Order updatedOrder = orderRepository.save(order);
        return OrderMapper.toDTO(updatedOrder);
    }

    public OrderDTO patchOrderById(UUID orderId, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        if (orderDTO.getOrderDate() != null) existingOrder.setOrderDate(orderDTO.getOrderDate());
        if (orderDTO.getTotalAmount() != null) existingOrder.setTotalAmount(orderDTO.getTotalAmount());
        if (orderDTO.getStatus() != null) {
            if (!isValidStatus(orderDTO.getStatus())) {
                throw new ValidationException("Invalid order status");
            }
            existingOrder.setStatus(orderDTO.getStatus());
        }

        Order updatedOrder = orderRepository.save(existingOrder);
        return OrderMapper.toDTO(updatedOrder);
    }

    public void deleteOrderById(UUID orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException("Order not found with ID: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }

    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderMapper::toDTO)
                .toList();
    }

    private void validateOrderDTO(OrderDTO orderDTO) {
        if (orderDTO.getOrderDate() == null) {
            throw new ValidationException("Order date cannot be null");
        }
        if (orderDTO.getTotalAmount() == null || orderDTO.getTotalAmount() <= 0) {
            throw new ValidationException("Total amount must be greater than 0");
        }
        if (orderDTO.getStatus() == null) {
            throw new ValidationException("Order status cannot be null");
        }
    }

    private boolean isValidStatus(OrderStatus status) {
        for (OrderStatus s : OrderStatus.values()) {
            if (s.equals(status)) {
                return true;
            }
        }
        return false;
    }
}
