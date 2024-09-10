package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.exception.ProductNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
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

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VendorRepository vendorRepository;


    public Product createProduct(Product product) {

        UUID vendorId = product.getVendor().getVendorID();
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ValidationException("Vendor with ID " + vendorId + " does not exist"));
        product.setVendor(vendor);


        UUID categoryId = product.getCategory().getCategoryID();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ValidationException("Category with ID " + categoryId + " does not exist"));
        product.setCategory(category);


        validateProduct(product);


        return productRepository.save(product);
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
    }

    public Product updateProduct(UUID id, Product product) {
        validateProduct(product);
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with ID " + id + " not found");
        }
        product.setProductID(id);
        return productRepository.save(product);
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductByName(String productName) {
        return productRepository.findByProductName(productName)
                .orElseThrow(() -> new ProductNotFoundException("Product with name " + productName + " not found"));
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
