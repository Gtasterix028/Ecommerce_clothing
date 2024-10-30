package com.gtasterix.E_Commerce.repository;

import com.gtasterix.E_Commerce.model.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VariantRepository extends JpaRepository<Variant, UUID> {
    List<Variant> findByColor(String color);
}
