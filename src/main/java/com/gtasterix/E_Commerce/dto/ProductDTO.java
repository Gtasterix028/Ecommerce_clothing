package com.gtasterix.E_Commerce.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductDTO {
    private UUID productID;
    private String productName;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private UUID categoryID;
    private String categoryName;
    private UUID vendorID;
    private String vendorName;
    private String imageURL;
    private String color;
    private String size;


}

