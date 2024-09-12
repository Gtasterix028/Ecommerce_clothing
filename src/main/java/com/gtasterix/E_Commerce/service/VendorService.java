package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.dto.VendorDTO;
import com.gtasterix.E_Commerce.exception.VendorNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.mapper.VendorMapper;
import com.gtasterix.E_Commerce.model.Vendor;
import com.gtasterix.E_Commerce.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    private static final Pattern CONTACT_INFO_PATTERN = Pattern.compile(
            "^[6-9][0-9]{9}$"
    );

    // Modify this method to accept VendorDTO
    public Vendor createVendor(VendorDTO vendorDTO) {
        Vendor vendor = VendorMapper.toEntity(vendorDTO);
        validateVendor(vendor);
        return vendorRepository.save(vendor);
    }

    public Vendor getVendorById(UUID id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with ID " + id + " not found"));
    }

    public Vendor updateVendor(UUID id, VendorDTO vendorDTO) {
        Vendor vendor = VendorMapper.toEntity(vendorDTO);
        validateVendor(vendor);
        if (!vendorRepository.existsById(id)) {
            throw new VendorNotFoundException("Vendor with ID " + id + " not found");
        }
        vendor.setVendorID(id);
        return vendorRepository.save(vendor);
    }

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    public Vendor patchVendorById(UUID id, VendorDTO vendorDTO) {
        Vendor existingVendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with ID " + id + " not found"));

        if (vendorDTO.getVendorName() != null) existingVendor.setVendorName(vendorDTO.getVendorName());
        if (vendorDTO.getContactInfo() != null) {
            if (!CONTACT_INFO_PATTERN.matcher(vendorDTO.getContactInfo()).matches()) {
                throw new ValidationException("Invalid contact info format");
            }
            existingVendor.setContactInfo(vendorDTO.getContactInfo());
        }

        return vendorRepository.save(existingVendor);
    }

    public void deleteVendorById(UUID id) {
        Vendor existingVendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with ID " + id + " not found"));
        vendorRepository.delete(existingVendor);
    }

    private void validateVendor(Vendor vendor) {
        if (vendor.getVendorName() == null || vendor.getVendorName().isEmpty()) {
            throw new ValidationException("Vendor name cannot be null or empty");
        }
        if (vendor.getContactInfo() == null || !CONTACT_INFO_PATTERN.matcher(vendor.getContactInfo()).matches()) {
            throw new ValidationException("Invalid contact info format");
        }
    }
}
