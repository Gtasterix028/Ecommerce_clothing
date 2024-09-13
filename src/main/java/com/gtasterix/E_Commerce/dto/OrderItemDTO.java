package com.gtasterix.E_Commerce.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class OrderItemDTO {
    private UUID orderItemID;
    private UUID orderID;
    private UUID productID;
    private Integer quantity;
    private Double price;
}
