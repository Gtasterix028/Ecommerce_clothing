package com.gtasterix.E_Commerce.mapper;

import com.gtasterix.E_Commerce.dto.OrderItemDTO;
import com.gtasterix.E_Commerce.model.OrderItem;
import com.gtasterix.E_Commerce.model.Order;
import com.gtasterix.E_Commerce.model.Product;

public class OrderItemMapper {

    public static OrderItemDTO toDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setOrderItemID(orderItem.getOrderItemID());
        dto.setOrderID(orderItem.getOrder().getOrderID());
        dto.setProductID(orderItem.getProduct().getProductID());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        return dto;
    }

    public static OrderItem toEntity(OrderItemDTO dto, Order order, Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemID(dto.getOrderItemID());
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPrice(dto.getPrice());
        return orderItem;
    }
}
