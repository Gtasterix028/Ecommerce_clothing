package com.gtasterix.E_Commerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "variant")
@Data
public class    Variant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID variantID;

    @ManyToOne
    @JoinColumn(name = "productID", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String color;


    private Double discount;

    @Column(nullable = false)
    private Double price;


    private String material;



    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SizeStock> sizeStockList = new ArrayList<>();


    @ElementCollection
    @CollectionTable(name = "product_image", joinColumns = @JoinColumn(name = "variant_id"))
    @Column(name = "image_url", length = 2048)
    private List<String> imageURLs = new ArrayList<>();

    public Variant() {}

    public Variant(UUID variantID, Product product, String color, Double discount, String material,List<SizeStock> sizeStockList) {
        this.variantID = (variantID != null) ? variantID : UUID.randomUUID();
        this.product = product;
        this.color = color;
        this.discount = discount;
        this.material=material;
        this.sizeStockList = sizeStockList;
        this.price = calculatePrice();
    }

    public void setSizeStockList(List<SizeStock> sizeStockList) {
        // Remove the reference from old SizeStock entities
        for (SizeStock stock : this.sizeStockList) {
            stock.setVariant(null);
        }
        this.sizeStockList.clear(); // Clear the existing list
        this.sizeStockList.addAll(sizeStockList); // Add new SizeStock entities
        for (SizeStock stock : sizeStockList) {
            stock.setVariant(this); // Set the back-reference
        }
    }

    @PrePersist
    public void prePersist() {
        if (this.price == null) {
            this.price = calculatePrice(); // Calculate price before persisting
        }
    }

    public Double calculatePrice() {
        if (product != null && product.getBasePrice() != null) {
            return product.getBasePrice() - (product.getBasePrice() * (this.discount / 100));
        }
        throw new IllegalArgumentException("Base price is not set for the product");
    }

    @Override
    public int hashCode() {
        return variantID != null ? variantID.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Variant variant = (Variant) obj;
        return variantID != null && variantID.equals(variant.variantID);
    }

    public void addSizeStock(SizeStock sizeStock) {
        sizeStock.setVariant(this);
        this.sizeStockList.add(sizeStock);
    }


    public void removeSizeStock(SizeStock sizeStock) {
        sizeStock.setVariant(null);
        this.sizeStockList.remove(sizeStock);
    }
}
