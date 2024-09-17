package com.gtasterix.E_Commerce.controller;

import com.gtasterix.E_Commerce.Util.Response;
import com.gtasterix.E_Commerce.dto.ProductDTO;
import com.gtasterix.E_Commerce.exception.NoProductFoundException;
import com.gtasterix.E_Commerce.exception.ProductNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Response> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            ProductDTO createdProduct = productService.createProduct(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new Response("Product created successfully", createdProduct, false));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response(e.getMessage(), null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getProductById(@PathVariable UUID id) {
        try {
            ProductDTO product = productService.getProductById(id);
            return ResponseEntity.ok(new Response("Product retrieved successfully", product, false));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage(), null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateProduct(@PathVariable UUID id, @RequestBody ProductDTO productDTO) {
        try {
            ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(new Response("Product updated successfully", updatedProduct, false));
        } catch (ProductNotFoundException | ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> patchProductById(@PathVariable UUID id, @RequestBody ProductDTO productDTO) {
        try {
            ProductDTO updatedProduct = productService.patchProductById(id, productDTO);
            return ResponseEntity.ok(new Response("Product updated successfully", updatedProduct, false));
        } catch (ProductNotFoundException | ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteProductById(@PathVariable UUID id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new Response("Product deleted successfully", null, false));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage(), null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @GetMapping
    public ResponseEntity<Response> getAllProducts() {
        try {
            List<ProductDTO> products = productService.getAllProducts();
            return ResponseEntity.ok(new Response("Products retrieved successfully", products, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @GetMapping("/api/products/filter")
    public ResponseEntity<Response> filterProducts(
            @RequestParam(required = false) UUID categoryID,
            @RequestParam(required = false) UUID vendorID,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String name) {

        try {
            List<ProductDTO> products = productService.filterProducts(categoryID, vendorID, minPrice, maxPrice, color, size, name);
            return ResponseEntity.ok(new Response("Filtered products retrieved successfully", products, false));
        } catch (NoProductFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(e.getMessage(), null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("An error occurred", e.getMessage(), true));
        }
    }
}
