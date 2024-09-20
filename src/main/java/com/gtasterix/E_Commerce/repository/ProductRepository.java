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
public interface ProductRepository extends JpaRepository<Product, UUID>,ProductCustomRepository {

    Optional<Product> findByProductName(String productName);

    @Query("SELECT p FROM Product p WHERE p.productName = :productName AND p.color = :color")
    List<Product> findByProductNameAndColor(@Param("productName") String productName, @Param("color") String color);
}




