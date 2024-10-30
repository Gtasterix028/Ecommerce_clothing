package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.dto.ProductDTO;
import com.gtasterix.E_Commerce.dto.SizeStockDTO;
import com.gtasterix.E_Commerce.dto.VariantDTO;
import com.gtasterix.E_Commerce.exception.ProductNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.mapper.ProductMapper;
import com.gtasterix.E_Commerce.mapper.SizeStockMapper;
import com.gtasterix.E_Commerce.mapper.VariantMapper;
import com.gtasterix.E_Commerce.model.Category;
import com.gtasterix.E_Commerce.model.Product;
import com.gtasterix.E_Commerce.model.SizeStock;
import com.gtasterix.E_Commerce.model.Variant;
import com.gtasterix.E_Commerce.model.Vendor;
import com.gtasterix.E_Commerce.repository.CategoryRepository;
import com.gtasterix.E_Commerce.repository.ProductRepository;
import com.gtasterix.E_Commerce.repository.VariantRepository;
import com.gtasterix.E_Commerce.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private VariantRepository variantRepository;

    public ProductDTO createProduct(ProductDTO productDTO) {
        validateProductDTO(productDTO);


        if (productDTO.getBasePrice()==null) {
            throw new IllegalArgumentException("Base price is not set for the product");
        }

        Category category = categoryRepository.findById(productDTO.getCategoryID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Category ID"));
        Vendor vendor = vendorRepository.findById(productDTO.getVendorID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Vendor ID"));

            Product product = ProductMapper.toEntity(productDTO, category, vendor);
        product.setBasePrice(productDTO.getBasePrice()); // Set basePrice
        productRepository.save(product);

        if (productDTO.getVariants() != null) {
            for (VariantDTO variantDTO : productDTO.getVariants()) {
                createVariant(product.getProductID(), variantDTO);
            }
        }

        return ProductMapper.toDTO(product);
    }


    public ProductDTO getProductById(UUID id) {
        return productRepository.findById(id)
                .map(ProductMapper::toDTO)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
    }

    public ProductDTO updateProduct(UUID id, ProductDTO productDTO) {
        validateProductDTO(productDTO);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        Category category = categoryRepository.findById(productDTO.getCategoryID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Category ID"));
        Vendor vendor = vendorRepository.findById(productDTO.getVendorID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Vendor ID"));

        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setCategory(category);
        product.setVendor(vendor);

        saveVariants(product, productDTO.getVariants());
        productRepository.save(product);

        return ProductMapper.toDTO(product);
    }

    public ProductDTO patchProductById(UUID id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        if (productDTO.getProductName() != null) {
            product.setProductName(productDTO.getProductName());
        }
        if (productDTO.getDescription() != null) {
            product.setDescription(productDTO.getDescription());
        }
        if (productDTO.getCategoryID() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryID())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Category ID"));
            product.setCategory(category);
        }
        if (productDTO.getVendorID() != null) {
            Vendor vendor = vendorRepository.findById(productDTO.getVendorID())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Vendor ID"));
            product.setVendor(vendor);
        }

        saveVariants(product, productDTO.getVariants());
        productRepository.save(product);

        return ProductMapper.toDTO(product);
    }

    public void deleteProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
        productRepository.delete(product);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductByName(String name) {
        return productRepository.findByProductName(name)
                .map(ProductMapper::toDTO)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with name: " + name));
    }

//    public Variant createVariant(UUID productId, VariantDTO variantDTO) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
//
//        // Ensure the base price is set before calculating the variant price
//        if (product.getBasePrice() == null) {
//            throw new ValidationException("Base price is not set for the product");
//        }
//
//        Variant variant = VariantMapper.toEntity(variantDTO, product);
//
//        if (variantDTO.getSizeStockList().isEmpty()) {
//            throw new ValidationException("SizeStock list cannot be empty");
//        }
//
//        // Validate SizeStock entities
//        for (SizeStockDTO sizeStockDTO : variantDTO.getSizeStockList()) {
//            if (sizeStockDTO.getSize() == null || sizeStockDTO.getStockQuantity() == null) {
//                throw new ValidationException("Invalid SizeStock entity");
//            }
//        }
//
//        // Calculate variant price based on base price and discount
//        variant.setPrice(variant.calculatePrice()); // Pass discount to calculatePrice
//
//        // Save the variant and associate SizeStock
//        Variant savedVariant = variantRepository.save(variant);
//        List<SizeStock> sizeStocks = createSizeStocks(variantDTO.getSizeStockList(), savedVariant);
//        savedVariant.setSizeStockList(sizeStocks);
//        return variantRepository.save(savedVariant);
//    }

    public Variant createVariant(UUID productId, VariantDTO variantDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Ensure the base price is set before calculating the variant price
        if (product.getBasePrice() == null) {
            throw new ValidationException("Base price is not set for the product");
        }

        Variant variant = VariantMapper.toEntity(variantDTO, product);

        if (variantDTO.getSizeStockList().isEmpty()) {
            throw new ValidationException("SizeStock list cannot be empty");
        }

        // Validate SizeStock entities
        for (SizeStockDTO sizeStockDTO : variantDTO.getSizeStockList()) {
            if (sizeStockDTO.getSize() == null || sizeStockDTO.getStockQuantity() == null) {
                throw new ValidationException("Invalid SizeStock entity");
            }
        }

        // Save the variant and associate SizeStock
        Variant savedVariant = variantRepository.save(variant);
        List<SizeStock> sizeStocks = createSizeStocks(variantDTO.getSizeStockList(), savedVariant);
        savedVariant.setSizeStockList(sizeStocks);

        // Calculate variant price based on base price and discount after saving the variant
        savedVariant.setPrice(savedVariant.calculatePrice());

        return variantRepository.save(savedVariant);
    }

    private List<SizeStock> createSizeStocks(List<SizeStockDTO> sizeStockDTOs, Variant variant) {
        List<SizeStock> sizeStocks = new ArrayList<>();

        if (sizeStockDTOs == null || sizeStockDTOs.isEmpty()) {
            throw new ValidationException("SizeStock list cannot be null or empty");
        }

        for (SizeStockDTO sizeStockDTO : sizeStockDTOs) {
            SizeStock sizeStock = new SizeStock(sizeStockDTO.getSize(), sizeStockDTO.getStockQuantity());
            sizeStock.setVariant(variant); // Set the variant reference
            sizeStocks.add(sizeStock); // Add the new SizeStock
        }

        return sizeStocks;
    }

    private void saveVariants(Product product, List<VariantDTO> variantDTOs) {
        if (variantDTOs != null) {
            for (VariantDTO variantDTO : variantDTOs) {
                Variant variant = VariantMapper.toEntity(variantDTO, product);
                // Clear existing sizeStockList to avoid orphan issues
                variant.setSizeStockList(new ArrayList<>());
                // Add new SizeStock entities
                for (SizeStockDTO sizeStockDTO : variantDTO.getSizeStockList()) {
                    SizeStock sizeStock = SizeStockMapper.toEntity(sizeStockDTO, variant);
                    variant.addSizeStock(sizeStock); // Use the add method
                }
                variantRepository.save(variant);
            }
        }
    }


    private void validateProductDTO(ProductDTO productDTO) {
        if (productDTO.getBasePrice() == null) {
            throw new IllegalArgumentException("Base price is not set for the product");
        }
        if (productDTO.getProductName() == null || productDTO.getProductName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
    }
}
