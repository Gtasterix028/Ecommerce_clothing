package com.gtasterix.E_Commerce.controller;

import com.gtasterix.E_Commerce.dto.VariantDTO;
import com.gtasterix.E_Commerce.model.Variant;
import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
public class ProductSingleColorDto {
    private UUID productID;
    private String productName;
    private String description;
    private UUID categoryID;
    private String categoryName;
    private UUID vendorID;
    private String vendorName;
    private List<String> imageURLs;
    private List<VariantDTO> variants;

//    public ProductSingleColorDto(Variant variant) {
//
//        this.imageURLs = variant.getProduct().getImageURLs();
//        this.vendorName = variant.getProduct().getVendor().getVendorName();
//        this.vendorID = variant.getProduct().getVendor().getVendorID();
//        this.categoryName = variant.getProduct().getCategory().getCategoryName();
//        this.categoryID = variant.getProduct().getCategory().getCategoryID();
//        this.description = variant.getProduct().getDescription();
//        this.productName = variant.getProduct().getProductName();
//        this.productID = variant.getProduct().getProductID();
//    }
}
