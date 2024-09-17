package com.gtasterix.E_Commerce.mapper;

import com.gtasterix.E_Commerce.dto.ProductDTO;
import com.gtasterix.E_Commerce.model.Category;
import com.gtasterix.E_Commerce.model.Product;
import com.gtasterix.E_Commerce.model.Vendor;

public class ProductMapper {
    public static ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductID(product.getProductID());
        dto.setProductName(product.getProductName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCategoryID(product.getCategory().getCategoryID());
        dto.setCategoryName(product.getCategory().getCategoryName());
        dto.setVendorID(product.getVendor().getVendorID());
        dto.setVendorName(product.getVendor().getVendorName());
        dto.setImageURL(product.getImageURL());
        dto.setColor(product.getColor());
        dto.setSize(product.getSize());
        return dto;
    }

    public static Product toEntity(ProductDTO dto, Category category, Vendor vendor) {
        Product product = new Product();
        product.setProductID(dto.getProductID());
        product.setProductName(dto.getProductName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setCategory(category);
        product.setVendor(vendor);
        product.setImageURL(dto.getImageURL());
        product.setColor(dto.getColor());
        product.setSize(dto.getSize());
        return product;
    }
}

