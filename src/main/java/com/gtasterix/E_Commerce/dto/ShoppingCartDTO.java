package com.gtasterix.E_Commerce.dto;

import lombok.Data;

import java.util.UUID;


@Data
public class ShoppingCartDTO {
    private UUID cartID;
    private UUID userID;
}