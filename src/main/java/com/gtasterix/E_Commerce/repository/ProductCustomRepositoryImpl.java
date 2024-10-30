package com.gtasterix.E_Commerce.repository.impl;

import com.gtasterix.E_Commerce.model.Product;
import com.gtasterix.E_Commerce.model.Variant;
import com.gtasterix.E_Commerce.repository.ProductCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Product> filterProducts(UUID categoryID, UUID vendorID, Double minPrice, Double maxPrice, String color, String size, String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> product = cq.from(Product.class);
        Join<Product, Variant> variantJoin = product.join("variants", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (categoryID != null) {
            predicates.add(cb.equal(product.get("category").get("categoryID"), categoryID));
        }
        if (vendorID != null) {
            predicates.add(cb.equal(product.get("vendor").get("vendorID"), vendorID));
        }
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(variantJoin.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(variantJoin.get("price"), maxPrice));
        }
        if (color != null) {
            predicates.add(cb.equal(variantJoin.get("color"), color));
        }
        if (size != null) {
            predicates.add(cb.equal(variantJoin.get("size"), size));
        }
        if (name != null) {
            predicates.add(cb.like(product.get("productName"), "%" + name + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public Optional<Product> filterProduct(UUID categoryID, UUID vendorID, Double minPrice, Double maxPrice, String color, String size, String name) {
        return Optional.empty();
    }
}
