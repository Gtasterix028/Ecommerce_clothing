package com.gtasterix.E_Commerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class SizeStock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID sizeStockId;

    private String size;
    private Integer stockQuantity;

    // Remove back reference from hashCode and equals
    @ManyToOne(optional = true)
    @JoinColumn(name = "variant_id")
    private Variant variant;

    public SizeStock( UUID sizeStockId, String size, Variant variant) {
      ;
        this.sizeStockId = sizeStockId;
        this.size = size;
        this.variant = variant;
    }

    public SizeStock() {
        // Empty constructor for JPA
    }

    public SizeStock(String size, Integer stockQuantity) {
        this.size = size;
        this.stockQuantity = stockQuantity;
    }

    @Override
    public int hashCode() {
        return sizeStockId != null ? sizeStockId.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SizeStock sizeStock = (SizeStock) obj;
        return sizeStockId != null && sizeStockId.equals(sizeStock.sizeStockId);
    }
}
