package com.gtasterix.E_Commerce.dto;

import com.gtasterix.E_Commerce.model.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderDTO {
    private UUID orderID;
    private UUID userID;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private OrderStatus status;
}
