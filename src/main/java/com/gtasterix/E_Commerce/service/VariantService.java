package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.dto.SizeStockDTO;
import com.gtasterix.E_Commerce.dto.VariantDTO;
import com.gtasterix.E_Commerce.exception.NoVariantFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.mapper.SizeStockMapper;
import com.gtasterix.E_Commerce.mapper.VariantMapper;
import com.gtasterix.E_Commerce.model.Product;
import com.gtasterix.E_Commerce.model.SizeStock;
import com.gtasterix.E_Commerce.model.Variant;
import com.gtasterix.E_Commerce.repository.ProductRepository;
import com.gtasterix.E_Commerce.repository.VariantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class VariantService {

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private ProductRepository productRepository;

    public VariantDTO createVariant(VariantDTO variantDTO) throws ValidationException {
        if (variantDTO.getProductID() == null) {
            throw new ValidationException("Product ID cannot be null");
        }

        Product product = productRepository.findById(variantDTO.getProductID())
                .orElseThrow(() -> new ValidationException("Product not found"));

        Variant variant = VariantMapper.toEntity(variantDTO, product);
        return VariantMapper.toDTO(variantRepository.save(variant));
    }

    public VariantDTO getVariantById(UUID id) {
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new NoVariantFoundException("Variant not found with ID: " + id));
        return VariantMapper.toDTO(variant);
    }
    public VariantDTO updateVariant(UUID id, VariantDTO variantDTO) {
        Variant existingVariant = variantRepository.findById(id)
                .orElseThrow(() -> new NoVariantFoundException("Variant not found with ID: " + id));

        existingVariant.setColor(variantDTO.getColor());
        existingVariant.setDiscount(variantDTO.getDiscount());
        existingVariant.setImageURLs(variantDTO.getImageURLs());
        existingVariant.setMaterial(variantDTO.getMaterial());

        // Clear existing sizeStockList to avoid orphan issues
        existingVariant.setSizeStockList(new ArrayList<>());

        // Add new SizeStock entities
        for (SizeStockDTO sizeStockDTO : variantDTO.getSizeStockList()) {
            SizeStock sizeStock = SizeStockMapper.toEntity(sizeStockDTO, existingVariant);
            existingVariant.addSizeStock(sizeStock); // Use the add method
        }

        return VariantMapper.toDTO(variantRepository.save(existingVariant));
    }

    public VariantDTO patchVariantById(UUID id, VariantDTO variantDTO) {
        Variant existingVariant = variantRepository.findById(id)
                .orElseThrow(() -> new NoVariantFoundException("Variant not found with ID: " + id));

        if (variantDTO.getColor() != null) {
            existingVariant.setColor(variantDTO.getColor());
        }
        if (variantDTO.getDiscount() != null) {
            existingVariant.setDiscount(variantDTO.getDiscount());
        }

        if (variantDTO.getImageURLs() != null) {
            existingVariant.setImageURLs(variantDTO.getImageURLs());
        }
        if(variantDTO.getMaterial() !=null) {
            existingVariant.setMaterial(variantDTO.getMaterial());
        }


        return VariantMapper.toDTO(variantRepository.save(existingVariant));
    }

    public void deleteVariantById(UUID id) {
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new NoVariantFoundException("Variant not found with ID: " + id));
        variantRepository.delete(variant);
    }

    public List<VariantDTO> getAllVariants() {
        return variantRepository.findAll().stream()
                .map(VariantMapper::toDTO)
                .toList();
    }
}
