package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.dto.ProductDTO;
import com.gtasterix.E_Commerce.exception.ProductNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.mapper.ProductMapper;
import com.gtasterix.E_Commerce.model.Category;
import com.gtasterix.E_Commerce.model.Product;
import com.gtasterix.E_Commerce.model.User;
import com.gtasterix.E_Commerce.model.Vendor;
import com.gtasterix.E_Commerce.repository.CategoryRepository;
import com.gtasterix.E_Commerce.repository.ProductRepository;
import com.gtasterix.E_Commerce.repository.UserRepository;
import com.gtasterix.E_Commerce.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        if (productDTO.getImageURL() != null) existingProduct.setImageURL(productDTO.getImageURL());

        Product updatedProduct = productRepository.save(existingProduct);
        return ProductMapper.toDTO(updatedProduct);
    }

//    public void deleteProductById(UUID productId) {
//        if (!productRepository.existsById(productId)) {
//            throw new ProductNotFoundException("Product with ID " + productId + " not found");
//        }
//        productRepository.deleteById(productId);
//    }





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

    public Product patchProductById(UUID id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));

        if (product.getProductName() != null) existingProduct.setProductName(product.getProductName());
        if (product.getDescription() != null) existingProduct.setDescription(product.getDescription());
        if (product.getPrice() != null) {
            if (product.getPrice() <= 0) {
                throw new ValidationException("Product price must be greater than zero");
            }
            existingProduct.setPrice(product.getPrice());
        }
        if (product.getStockQuantity() != null) {
            if (product.getStockQuantity() < 0) {
                throw new ValidationException("Product stock quantity cannot be negative");
            }
            existingProduct.setStockQuantity(product.getStockQuantity());
        }
        if (product.getCategory() != null) {

            existingProduct.setCategory(product.getCategory());
        }
        if (product.getVendor() != null) {

            existingProduct.setVendor(product.getVendor());
        }
        if (product.getImageURL() != null) existingProduct.setImageURL(product.getImageURL());

        return productRepository.save(existingProduct);
    }


    public void deleteProductById(UUID productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found"));

        productRepository.delete(existingProduct);
    }
}
