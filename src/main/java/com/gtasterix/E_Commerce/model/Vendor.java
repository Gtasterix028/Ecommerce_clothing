package com.gtasterix.E_Commerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "vendor")
@Data
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID vendorID;

    @Column(nullable = false)
    private String vendorName;

    private String contactInfo;

    private String address;

    @Column(nullable = false, unique = true)
    private String email;
}
