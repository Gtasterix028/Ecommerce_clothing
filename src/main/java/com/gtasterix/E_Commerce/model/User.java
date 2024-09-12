package com.gtasterix.E_Commerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userID;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "full_name")
    private String fullName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String mobileNumber;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role; // Enum: ADMIN, CUSTOMER, VENDOR, etc.


}


