package com.gtasterix.E_Commerce.mapper;

import com.gtasterix.E_Commerce.dto.SizeStockDTO;
import com.gtasterix.E_Commerce.dto.VariantDTO;
import com.gtasterix.E_Commerce.model.SizeStock;
import com.gtasterix.E_Commerce.model.Variant;
import com.gtasterix.E_Commerce.model.Product;


import java.util.List;

import java.util.stream.Collectors;

public class VariantMapper {

    public static VariantDTO toDTO(Variant variant) {
        VariantDTO dto = new VariantDTO();
        dto.setVariantID(variant.getVariantID());
        dto.setProductID(variant.getProduct().getProductID());
        dto.setColor(variant.getColor());
        dto.setDiscount(variant.getDiscount());
        dto.setPrice(variant.getPrice());
        dto.setMaterial(variant.getMaterial());
        dto.setSizeStockList(variant.getSizeStockList().stream()
                .map(sizeStock -> {
                    SizeStockDTO sizeStockDTO = new SizeStockDTO();
                    sizeStockDTO.setSize(sizeStock.getSize());
                    sizeStockDTO.setStockQuantity(sizeStock.getStockQuantity());
                    return sizeStockDTO;
                })
                .collect(Collectors.toList()));
        dto.setImageURLs(variant.getImageURLs());
        return dto;
    }

    public static Variant toEntity(VariantDTO dto, Product product) {
        Variant variant = new Variant();
        variant.setVariantID(dto.getVariantID());
        variant.setProduct(product);
        variant.setColor(dto.getColor());
        variant.setDiscount(dto.getDiscount());
        variant.setPrice(dto.getPrice());
        variant.setMaterial(dto.getMaterial());

        // Convert SizeStockDTO to SizeStock
        List<SizeStock> sizeStocks = dto.getSizeStockList().stream()
                .map(sizeStockDTO -> {
                    SizeStock sizeStock = new SizeStock();
                    sizeStock.setSize(sizeStockDTO.getSize());
                    sizeStock.setStockQuantity(sizeStockDTO.getStockQuantity());
                    sizeStock.setVariant(variant); // Set the variant reference
                    return sizeStock;
                })
                .collect(Collectors.toList());
        variant.setSizeStockList(sizeStocks);

        variant.setImageURLs(dto.getImageURLs());
        return variant;
    }
}