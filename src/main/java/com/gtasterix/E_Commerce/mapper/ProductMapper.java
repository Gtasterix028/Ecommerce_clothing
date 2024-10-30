package com.gtasterix.E_Commerce.mapper;

import com.gtasterix.E_Commerce.dto.ProductDTO;
import com.gtasterix.E_Commerce.dto.VariantDTO;
import com.gtasterix.E_Commerce.model.Category;
import com.gtasterix.E_Commerce.model.Product;
import com.gtasterix.E_Commerce.model.Vendor;
import com.gtasterix.E_Commerce.model.Variant;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    // Method to map ProductDTO to Product entity
    public static Product toEntity(ProductDTO productDTO, Category category, Vendor vendor) {
        Product product = new Product();
        product.setProductID(productDTO.getProductID());
        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setCategory(category);
        product.setVendor(vendor);
        product.setBasePrice(productDTO.getBasePrice());

        // Map VariantDTOs to Variants and associate them with the product
        List<Variant> variants = productDTO.getVariants().stream()
                .map(variantDTO -> toVariantEntity(variantDTO, product))  // Map each VariantDTO to Variant entity
                .collect(Collectors.toList());
        product.setVariants(variants);

        return product;
    }

    // Method to map Product entity to ProductDTO
    public static ProductDTO toDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductID(product.getProductID());
        productDTO.setProductName(product.getProductName());
        productDTO.setDescription(product.getDescription());
        productDTO.setCategoryID(product.getCategory().getCategoryID());
        productDTO.setCategoryName(product.getCategory().getCategoryName());
        productDTO.setVendorID(product.getVendor().getVendorID());
        productDTO.setVendorName(product.getVendor().getVendorName());
        productDTO.setBasePrice(product.getBasePrice());

        // Map Variants to VariantDTOs
        productDTO.setVariants(product.getVariants().stream()
                .map(variant -> VariantMapper.toDTO(variant))
                .collect(Collectors.toList()));

        return productDTO;
    }

    // Helper method to map VariantDTO to Variant entity
    private static Variant toVariantEntity(VariantDTO variantDTO, Product product) {
        Variant variant = new Variant();
        variant.setVariantID(variantDTO.getVariantID());
        variant.setProduct(product);  // Set the association with the product
        variant.setColor(variantDTO.getColor());
        variant.setDiscount(variantDTO.getDiscount());
        variant.setMaterial(variantDTO.getMaterial());


        // Set price based on provided price or calculate it
        if (variantDTO.getPrice() == null) {
            variant.setPrice(variant.calculatePrice());
        } else {
            variant.setPrice(variantDTO.getPrice());
        }

        // Map SizeStockDTO to SizeStock and pass the variant as a reference
        variant.setSizeStockList(variantDTO.getSizeStockList().stream()
                .map(sizeStockDTO -> SizeStockMapper.toEntity(sizeStockDTO, variant)) // Pass the variant reference
                .collect(Collectors.toList()));  // Set size and stock list

        variant.setImageURLs(variantDTO.getImageURLs());  // Set image URLs
        return variant;
    }
}
