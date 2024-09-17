package com.gtasterix.E_Commerce.repository;

import com.gtasterix.E_Commerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByProductName(String productName);


    @Query("SELECT p FROM Product p WHERE " +
            "(:categoryID IS NULL OR p.category.categoryID = :categoryID) AND " +
            "(:vendorID IS NULL OR p.vendor.vendorID = :vendorID) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:color IS NULL OR p.color = :color) AND " +
            "(:size IS NULL OR p.size = :size) AND " +
            "(:name IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :name, '%')))")
    List<Product> filterProducts(@Param("categoryID") UUID categoryID,
                                 @Param("vendorID") UUID vendorID,
                                 @Param("minPrice") Double minPrice,
                                 @Param("maxPrice") Double maxPrice,
                                 @Param("color") String color,
                                 @Param("size") String size,
                                 @Param("name") String name);
}

