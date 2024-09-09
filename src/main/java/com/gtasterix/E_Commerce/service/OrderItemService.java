package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.exception.OrderItemNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.model.OrderItem;
import com.gtasterix.E_Commerce.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public OrderItem createOrderItem(OrderItem orderItem) {
        validateOrderItem(orderItem);
        return orderItemRepository.save(orderItem);
    }

    public OrderItem getOrderItemById(UUID id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException("OrderItem with ID " + id + " not found"));
    }

    public OrderItem updateOrderItem(UUID id, OrderItem orderItem) {
        validateOrderItem(orderItem);
        if (!orderItemRepository.existsById(id)) {
            throw new OrderItemNotFoundException("OrderItem with ID " + id + " not found");
        }
        orderItem.setOrderItemID(id);
        return orderItemRepository.save(orderItem);
    }

    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    private void validateOrderItem(OrderItem orderItem) {
        if (orderItem.getOrder() == null) {
            throw new ValidationException("Order cannot be null");
        }
        if (orderItem.getProduct() == null) {
            throw new ValidationException("Product cannot be null");
        }
        if (orderItem.getQuantity() == null || orderItem.getQuantity() <= 0) {
            throw new ValidationException("Quantity must be greater than zero");
        }
        if (orderItem.getPrice() == null || orderItem.getPrice() <= 0) {
            throw new ValidationException("Price must be greater than zero");
        }
    }

    public void deleteOrderItemById(UUID id) {
        OrderItem existingOrderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException("OrderItem with ID " + id + " not found"));

        orderItemRepository.delete(existingOrderItem);
    }
}
