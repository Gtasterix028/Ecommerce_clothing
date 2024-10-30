package com.gtasterix.E_Commerce.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class VariantDTO {
    private UUID variantID;
    private UUID productID;
    private String color;
    private Double discount; // Discount to apply to base price
    private Double price;
    private String material;
    private List<SizeStockDTO> sizeStockList;
    private List<String> imageURLs;

    public VariantDTO() {}

    public VariantDTO(UUID variantID, UUID productID, String color,Double discount, Double price,String material, List<SizeStockDTO> sizeStockList, List<String> imageURLs) {
        this.variantID = variantID;
        this.productID = productID;
        this.color = color;
        this.discount=discount;
        this.material=material;
        this.price = price;
        this.sizeStockList = sizeStockList;
        this.imageURLs = imageURLs;
    }
}
