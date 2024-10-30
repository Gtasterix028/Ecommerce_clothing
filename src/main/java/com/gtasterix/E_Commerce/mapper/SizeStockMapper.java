package com.gtasterix.E_Commerce.mapper;

import com.gtasterix.E_Commerce.dto.SizeStockDTO;
import com.gtasterix.E_Commerce.model.SizeStock;
import com.gtasterix.E_Commerce.model.Variant;

public class SizeStockMapper {

    // Method to map SizeStock entity to SizeStockDTO
    public static SizeStockDTO toDTO(SizeStock sizeStock) {
        SizeStockDTO dto = new SizeStockDTO();
       // dto.setSizeStockId(sizeStock.getSizeStockId());
        dto.setSize(sizeStock.getSize());
        dto.setStockQuantity(sizeStock.getStockQuantity());
        return dto;
    }

    // Method to map SizeStockDTO to SizeStock entity
    public static SizeStock toEntity(SizeStockDTO dto, Variant variant) {
        SizeStock sizeStock = new SizeStock();
        sizeStock.setSize(dto.getSize());
        sizeStock.setStockQuantity(dto.getStockQuantity());
        sizeStock.setVariant(variant); // Set the back-reference
        return sizeStock;
    }
}
