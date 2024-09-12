package com.gtasterix.E_Commerce.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class VendorDTO {
    private UUID vendorID;
    private String vendorName;
    private String contactInfo;


}
