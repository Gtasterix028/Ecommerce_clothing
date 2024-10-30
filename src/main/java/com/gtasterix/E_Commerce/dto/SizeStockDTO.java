package com.gtasterix.E_Commerce.dto;

import java.util.UUID;

public class SizeStockDTO {
   // private UUID sizeStockId;
    private String size;      // Size of the product (e.g., "S", "M", "L")
    private Integer stockQuantity; // Stock quantity

    // Default constructor
    public SizeStockDTO() {}

    // Parameterized constructor
    public SizeStockDTO(String size, int stockQuantity) {
        this.size = size;
        this.stockQuantity = stockQuantity;
    }

    public SizeStockDTO(UUID sizeStockId, String size, int stockQuantity) {
        //this.sizeStockId = sizeStockId;
        this.size = size;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
//    public UUID getSizeStockId() {
//        return sizeStockId;
//    }
//
//    public void setSizeStockId(UUID sizeStockId) {
//        this.sizeStockId = sizeStockId;
//    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
