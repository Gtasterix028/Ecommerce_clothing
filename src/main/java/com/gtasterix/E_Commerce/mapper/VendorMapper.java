package com.gtasterix.E_Commerce.mapper;

import com.gtasterix.E_Commerce.dto.VendorDTO;
import com.gtasterix.E_Commerce.model.Vendor;

public class VendorMapper {
    public static VendorDTO toDTO(Vendor vendor) {
        VendorDTO dto = new VendorDTO();
        dto.setVendorID(vendor.getVendorID());
        dto.setVendorName(vendor.getVendorName());
        dto.setContactInfo(vendor.getContactInfo());
        dto.setAddress(vendor.getAddress());
        dto.setEmail(vendor.getEmail());
        return dto;
    }

    public static Vendor toEntity(VendorDTO dto) {
        Vendor vendor = new Vendor();
        vendor.setVendorID(dto.getVendorID());
        vendor.setVendorName(dto.getVendorName());
        vendor.setContactInfo(dto.getContactInfo());
        vendor.setAddress(dto.getAddress());
        vendor.setEmail(dto.getEmail());
        return vendor;
    }
}
