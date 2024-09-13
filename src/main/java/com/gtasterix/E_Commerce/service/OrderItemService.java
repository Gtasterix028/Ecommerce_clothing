package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.dto.OrderItemDTO;
import com.gtasterix.E_Commerce.exception.OrderItemNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.mapper.OrderItemMapper;
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
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO) {
        try {
            Order order = orderRepository.findById(orderItemDTO.getOrderID())
                    .orElseThrow(() -> new ValidationException("Order with ID " + orderItemDTO.getOrderID() + " not found"));
            Product product = productRepository.findById(orderItemDTO.getProductID())
                    .orElseThrow(() -> new ValidationException("Product with ID " + orderItemDTO.getProductID() + " not found"));

            OrderItem orderItem = OrderItemMapper.toEntity(orderItemDTO, order, product);
            OrderItem createdOrderItem = orderItemRepository.save(orderItem);
            return OrderItemMapper.toDTO(createdOrderItem);
        } catch (ValidationException e) {
            throw new ValidationException("Failed to create order item: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while creating order item: " + e.getMessage());
        }
    }

    public OrderItemDTO getOrderItemById(UUID id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException("OrderItem with ID " + id + " not found"));
        return OrderItemMapper.toDTO(orderItem);
    }

    public OrderItemDTO updateOrderItem(UUID id, OrderItemDTO orderItemDTO) {
        validateOrderItem(orderItemDTO);
        if (!orderItemRepository.existsById(id)) {
            throw new OrderItemNotFoundException("OrderItem with ID " + id + " not found");
        }

        Order order = orderRepository.findById(orderItemDTO.getOrderID())
                .orElseThrow(() -> new ValidationException("Order with ID " + orderItemDTO.getOrderID() + " not found"));
        Product product = productRepository.findById(orderItemDTO.getProductID())
                .orElseThrow(() -> new ValidationException("Product with ID " + orderItemDTO.getProductID() + " not found"));

        OrderItem orderItem = OrderItemMapper.toEntity(orderItemDTO, order, product);
        orderItem.setOrderItemID(id);
        OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
        return OrderItemMapper.toDTO(updatedOrderItem);
    }

    public List<OrderItemDTO> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemRepository.findAll();
        return orderItems.stream().map(OrderItemMapper::toDTO).collect(Collectors.toList());
    }

    private void validateOrderItem(OrderItemDTO orderItemDTO) {
        if (orderItemDTO.getOrderID() == null) {
            throw new ValidationException("Order ID cannot be null");
        }
        if (orderItemDTO.getProductID() == null) {
            throw new ValidationException("Product ID cannot be null");
        }
        if (orderItemDTO.getQuantity() == null || orderItemDTO.getQuantity() <= 0) {
            throw new ValidationException("Quantity must be greater than zero");
        }
        if (orderItemDTO.getPrice() == null || orderItemDTO.getPrice() <= 0) {
            throw new ValidationException("Price must be greater than zero");
        }
    }

    public void deleteOrderItemById(UUID id) {
        OrderItem existingOrderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException("OrderItem with ID " + id + " not found"));

        orderItemRepository.delete(existingOrderItem);
    }

    public OrderItemDTO patchOrderItem(UUID id, OrderItemDTO orderItemDTO) {
        try {
            OrderItem existingOrderItem = orderItemRepository.findById(id)
                    .orElseThrow(() -> new OrderItemNotFoundException("Order item with ID " + id + " not found"));

            if (orderItemDTO.getOrderID() != null) {
                Order order = orderRepository.findById(orderItemDTO.getOrderID())
                        .orElseThrow(() -> new ValidationException("Order with ID " + orderItemDTO.getOrderID() + " not found"));
                existingOrderItem.setOrder(order);
            }

            if (orderItemDTO.getProductID() != null) {
                Product product = productRepository.findById(orderItemDTO.getProductID())
                        .orElseThrow(() -> new ValidationException("Product with ID " + orderItemDTO.getProductID() + " not found"));
                existingOrderItem.setProduct(product);
            }

            if (orderItemDTO.getQuantity() != null && orderItemDTO.getQuantity() > 0) {
                existingOrderItem.setQuantity(orderItemDTO.getQuantity());
            } else if (orderItemDTO.getQuantity() != null) {
                throw new ValidationException("Quantity must be greater than 0");
            }

            if (orderItemDTO.getPrice() != null && orderItemDTO.getPrice() > 0) {
                existingOrderItem.setPrice(orderItemDTO.getPrice());
            } else if (orderItemDTO.getPrice() != null) {
                throw new ValidationException("Price must be greater than 0");
            }

            OrderItem updatedOrderItem = orderItemRepository.save(existingOrderItem);
            return OrderItemMapper.toDTO(updatedOrderItem);
        } catch (OrderItemNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while patching order item: " + e.getMessage());
        }
    }
}
