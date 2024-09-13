package com.gtasterix.E_Commerce.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CartItemDTO {
    private UUID cartItemID;
    private UUID cartID;
    private UUID productID;
    private Integer quantity;
}
