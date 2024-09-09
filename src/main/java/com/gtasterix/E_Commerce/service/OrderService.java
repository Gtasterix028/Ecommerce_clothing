package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.exception.OrderNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.model.Order;
import com.gtasterix.E_Commerce.model.OrderStatus;
import com.gtasterix.E_Commerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        try {
            validateOrder(order);
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new RuntimeException("Error creating order", e);
        }
    }

    public Order getOrderById(UUID orderId) {
        try {
            return orderRepository.findById(orderId)
                    .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving order", e);
        }
    }

    public Order updateOrderById(UUID orderId, Order order) {
        try {
            validateOrder(order);
            if (!orderRepository.existsById(orderId)) {
                throw new OrderNotFoundException("Order not found with ID: " + orderId);
            }
            order.setOrderID(orderId); // Ensure ID is not modified
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new RuntimeException("Error updating order", e);
        }
    }

    public Order patchOrderById(UUID orderId, Order order) {
        try {
            Order existingOrder = orderRepository.findById(orderId)
                    .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

            if (order.getOrderDate() != null) existingOrder.setOrderDate(order.getOrderDate());
            if (order.getTotalAmount() != null) existingOrder.setTotalAmount(order.getTotalAmount());
            if (order.getStatus() != null) {
                if (!isValidStatus(order.getStatus())) {
                    throw new ValidationException("Invalid order status");
                }
                existingOrder.setStatus(order.getStatus());
            }

            return orderRepository.save(existingOrder);
        } catch (Exception e) {
            throw new RuntimeException("Error patching order", e);
        }
    }

    public void deleteOrderById(UUID orderId) {
        try {
            if (!orderRepository.existsById(orderId)) {
                throw new OrderNotFoundException("Order not found with ID: " + orderId);
            }
            orderRepository.deleteById(orderId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting order", e);
        }
    }

    public List<Order> getAllOrders() {
        try {
            return orderRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all orders", e);
        }
    }

    private void validateOrder(Order order) {
        if (order.getOrderDate() == null) {
            throw new ValidationException("Order date cannot be null");
        }
        if (order.getTotalAmount() == null || order.getTotalAmount() <= 0) {
            throw new ValidationException("Total amount must be greater than 0");
        }
        if (order.getStatus() == null) {
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
