package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.exception.OrderItemNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.model.Order;
import com.gtasterix.E_Commerce.model.OrderItem;
import com.gtasterix.E_Commerce.model.Product;
import com.gtasterix.E_Commerce.repository.OrderItemRepository;
import com.gtasterix.E_Commerce.repository.OrderRepository;
import com.gtasterix.E_Commerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public OrderItem createOrderItem(OrderItem orderItem) {
        try
        {
            Order order = orderRepository.findById(orderItem.getOrder().getOrderID())
                    .orElseThrow(() -> new ValidationException("Order with ID " + orderItem.getOrder().getOrderID() + " not found"));
            orderItem.setOrder(order);
            Product product = productRepository.findById(orderItem.getProduct().getProductID())
                    .orElseThrow(() -> new ValidationException("Product with ID " + orderItem.getProduct().getProductID() + " not found"));
            orderItem.setProduct(product);
            return orderItemRepository.save(orderItem);
        }
        catch (ValidationException e) {
            throw new ValidationException("Failed to create order item: " + e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while creating order item: " + e.getMessage());
        }
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

    public OrderItem patchOrderItem(UUID id, OrderItem orderItem) {
        try {

            OrderItem existingOrderItem = orderItemRepository.findById(id)
                    .orElseThrow(() -> new OrderItemNotFoundException("Order item with ID " + id + " not found"));


            if (orderItem.getOrder() != null) {
                Order order = orderRepository.findById(orderItem.getOrder().getOrderID())
                        .orElseThrow(() -> new ValidationException("Order with ID " + orderItem.getOrder().getOrderID() + " not found"));
                existingOrderItem.setOrder(order);
            }

            if (orderItem.getProduct() != null) {
                Product product = productRepository.findById(orderItem.getProduct().getProductID())
                        .orElseThrow(() -> new ValidationException("Product with ID " + orderItem.getProduct().getProductID() + " not found"));
                existingOrderItem.setProduct(product);
            }

            if (orderItem.getQuantity() != null && orderItem.getQuantity() > 0) {
                existingOrderItem.setQuantity(orderItem.getQuantity());
            } else if (orderItem.getQuantity() != null) {
                throw new ValidationException("Quantity must be greater than 0");
            }

            if (orderItem.getPrice() != null && orderItem.getPrice() > 0) {
                existingOrderItem.setPrice(orderItem.getPrice());
            } else if (orderItem.getPrice() != null) {
                throw new ValidationException("Price must be greater than 0");
            }


            return orderItemRepository.save(existingOrderItem);
        } catch (OrderItemNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while patching order item: " + e.getMessage());
        }
    }

}
