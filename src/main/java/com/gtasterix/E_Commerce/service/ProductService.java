package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.dto.ProductDTO;
import com.gtasterix.E_Commerce.exception.NoProductFoundException;
import com.gtasterix.E_Commerce.exception.ProductNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.mapper.ProductMapper;
import com.gtasterix.E_Commerce.model.Category;
import com.gtasterix.E_Commerce.model.Product;
import com.gtasterix.E_Commerce.model.Vendor;
import com.gtasterix.E_Commerce.repository.CategoryRepository;
import com.gtasterix.E_Commerce.repository.ProductRepository;
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

    public ProductDTO createProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryID())
                .orElseThrow(() -> new ValidationException("Category with ID " + productDTO.getCategoryID() + " does not exist"));
        Vendor vendor = vendorRepository.findById(productDTO.getVendorID())
                .orElseThrow(() -> new ValidationException("Vendor with ID " + productDTO.getVendorID() + " does not exist"));

        Product product = ProductMapper.toEntity(productDTO, category, vendor);
        validateProduct(product);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toDTO(savedProduct);
    }

    public ProductDTO getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
        return ProductMapper.toDTO(product);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO updateProduct(UUID id, ProductDTO productDTO) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with ID " + id + " not found");
        }

        Category category = categoryRepository.findById(productDTO.getCategoryID())
                .orElseThrow(() -> new ValidationException("Category with ID " + productDTO.getCategoryID() + " does not exist"));
        Vendor vendor = vendorRepository.findById(productDTO.getVendorID())
                .orElseThrow(() -> new ValidationException("Vendor with ID " + productDTO.getVendorID() + " does not exist"));

        Product product = ProductMapper.toEntity(productDTO, category, vendor);
        product.setProductID(id);
        validateProduct(product);
        Product updatedProduct = productRepository.save(product);
        return ProductMapper.toDTO(updatedProduct);
    }

    public ProductDTO patchProductById(UUID id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));

        if (productDTO.getProductName() != null) existingProduct.setProductName(productDTO.getProductName());
        if (productDTO.getDescription() != null) existingProduct.setDescription(productDTO.getDescription());
        if (productDTO.getPrice() != null) {
            if (productDTO.getPrice() <= 0) {
                throw new ValidationException("Product price must be greater than zero");
            }
            existingProduct.setPrice(productDTO.getPrice());
        }
        if (productDTO.getStockQuantity() != null) {
            if (productDTO.getStockQuantity() < 0) {
                throw new ValidationException("Product stock quantity cannot be negative");
            }
            existingProduct.setStockQuantity(productDTO.getStockQuantity());
        }
        if (productDTO.getColor() != null) existingProduct.setColor(productDTO.getColor());
        if (productDTO.getSize() != null) existingProduct.setSize(productDTO.getSize());
        if (productDTO.getCategoryID() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryID())
                    .orElseThrow(() -> new ValidationException("Category with ID " + productDTO.getCategoryID() + " does not exist"));
            existingProduct.setCategory(category);
        }
        if (productDTO.getVendorID() != null) {
            Vendor vendor = vendorRepository.findById(productDTO.getVendorID())
                    .orElseThrow(() -> new ValidationException("Vendor with ID " + productDTO.getVendorID() + " does not exist"));
            existingProduct.setVendor(vendor);
        }
        if (productDTO.getImageURLs() != null) existingProduct.setImageURLs(productDTO.getImageURLs());

        Product updatedProduct = productRepository.save(existingProduct);
        return ProductMapper.toDTO(updatedProduct);
    }

    public void deleteProductById(UUID productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found"));

        productRepository.delete(existingProduct);
    }

    private void validateProduct(Product product) {
        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            throw new ValidationException("Product name cannot be null or empty");
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new ValidationException("Product price must be greater than zero");
        }
        if (product.getStockQuantity() == null || product.getStockQuantity() < 0) {
            throw new ValidationException("Product stock quantity cannot be negative");
        }
    }


    public List<ProductDTO> filterProducts(UUID categoryID, UUID vendorID, Double minPrice, Double maxPrice, String color, String size, String name) {
        List<Product> filteredProducts = productRepository.filterProducts(categoryID, vendorID, minPrice, maxPrice, color, size, name);
        if (filteredProducts.isEmpty()) {
            throw new NoProductFoundException("No products match the filter criteria");
        }
        return
                filteredProducts.stream().map(ProductMapper::toDTO).collect(Collectors.toList());
    }

    public ProductDTO getProductByName(String name) {
        Product product = productRepository.findByProductName(name)
                .orElseThrow(() -> new ProductNotFoundException("Product with name " + name + " not found"));
        return ProductMapper.toDTO(product);
    }


    public List<String> getImageURLsByNameAndColor(String productName, String color) {
        List<Product> products = productRepository.findByProductNameAndColor(productName, color);

        if (products.isEmpty()) {
            throw new NoProductFoundException("No products match the provided name and color");
        }


        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = ProductMapper.toDTO(product);
            productDTOs.add(productDTO);
        }


        List<String> imageURLs = new ArrayList<>();
        for (ProductDTO productDTO : productDTOs) {
            imageURLs.addAll(productDTO.getImageURLs());
        }

        return imageURLs;
    }




}
