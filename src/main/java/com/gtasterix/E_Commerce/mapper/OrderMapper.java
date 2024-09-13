package com.gtasterix.E_Commerce.mapper;

import com.gtasterix.E_Commerce.dto.OrderDTO;
import com.gtasterix.E_Commerce.model.Order;
import com.gtasterix.E_Commerce.model.User;

public class OrderMapper {

    public static OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderID(order.getOrderID());
        dto.setUserID(order.getUser().getUserID());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        return dto;
    }

    public static Order toEntity(OrderDTO dto, User user) {
        Order order = new Order();
        order.setOrderID(dto.getOrderID());
        order.setUser(user);
        order.setOrderDate(dto.getOrderDate());
        order.setTotalAmount(dto.getTotalAmount());
        order.setStatus(dto.getStatus());
        return order;
    }
}
