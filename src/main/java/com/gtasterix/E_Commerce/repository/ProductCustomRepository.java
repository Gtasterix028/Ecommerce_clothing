package com.gtasterix.E_Commerce.repository;

import com.gtasterix.E_Commerce.model.Product;

import java.util.List;
import java.util.UUID;

public interface ProductCustomRepository {
    List<Product> filterProducts(UUID categoryID, UUID vendorID, Double minPrice, Double maxPrice, String color, String size, String name);
}
