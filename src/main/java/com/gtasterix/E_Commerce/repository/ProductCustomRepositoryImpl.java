package com.gtasterix.E_Commerce.repository;

import com.gtasterix.E_Commerce.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> filterProducts(UUID categoryID, UUID vendorID, Double minPrice, Double maxPrice, String color, String size, String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> product = cq.from(Product.class);

        // Create a list of predicates (conditions) based on the optional filter parameters
        List<Predicate> predicates = new ArrayList<>();

        if (categoryID != null) {
            predicates.add(cb.equal(product.get("category").get("categoryID"), categoryID));
        }

        if (vendorID != null) {
            predicates.add(cb.equal(product.get("vendor").get("vendorID"), vendorID));
        }

        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(product.get("price"), minPrice));
        }

        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(product.get("price"), maxPrice));
        }

        if (color != null) {
            predicates.add(cb.equal(product.get("color"), color));
        }

        if (size != null) {
            predicates.add(cb.equal(product.get("size"), size));
        }

        if (name != null) {
            predicates.add(cb.like(cb.lower(product.get("productName")), "%" + name.toLowerCase() + "%"));
        }

        // Apply predicates (conditions) to the query
        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }
}
