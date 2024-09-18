package com.gtasterix.E_Commerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productID;

    @Column(nullable = false)
    private String productName;

    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String size;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryID", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendorID", nullable = false)
    private Vendor vendor;

    @ElementCollection@CollectionTable(name = "product_images",
            joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url", length=2048)
    private List<String> imageURLs;

}
