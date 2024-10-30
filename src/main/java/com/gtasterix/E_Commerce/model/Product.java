package com.gtasterix.E_Commerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID productID;

    private String productName;

    private String description;

    private Double basePrice; // Base price for the product

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

//    @ElementCollection
//    @CollectionTable(name = "product_image", joinColumns = @JoinColumn(name = "product_id"))
//    @Column(name = "image_url",length = 2048)
//    private List<String> imageURLs = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Variant> variants = new ArrayList<>();
}
