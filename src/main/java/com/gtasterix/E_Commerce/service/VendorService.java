package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.exception.VendorNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.model.Vendor;
import com.gtasterix.E_Commerce.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.UUID;

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

    public Vendor createVendor(Vendor vendor) {
        validateVendor(vendor);
        return vendorRepository.save(vendor);
    }

    public Vendor getVendorById(UUID id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with ID " + id + " not found"));
    }

    public Vendor updateVendor(UUID id, Vendor vendor) {
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

    private void validateVendor(Vendor vendor) {
        if (vendor.getVendorName() == null || vendor.getVendorName().isEmpty()) {
            throw new ValidationException("Vendor name cannot be null or empty");
        }
        if (vendor.getEmail() == null || !EMAIL_PATTERN.matcher(vendor.getEmail()).matches()) {
            throw new ValidationException("Invalid email format");
        }
        if (vendor.getContactInfo() == null || !CONTACT_INFO_PATTERN.matcher(vendor.getContactInfo()).matches()) {
            throw new ValidationException("Invalid contact info format");
        }
        // Additional validation for address if needed
    }

    public Vendor patchVendorById(UUID id, Vendor vendor) {
        Vendor existingVendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with ID " + id + " not found"));

        if (vendor.getVendorName() != null) existingVendor.setVendorName(vendor.getVendorName());
        if (vendor.getContactInfo() != null) {
            if (!CONTACT_INFO_PATTERN.matcher(vendor.getContactInfo()).matches()) {
                throw new ValidationException("Invalid contact info format");
            }
            existingVendor.setContactInfo(vendor.getContactInfo());
        }
        if (vendor.getAddress() != null) existingVendor.setAddress(vendor.getAddress());
        if (vendor.getEmail() != null) {
            if (!EMAIL_PATTERN.matcher(vendor.getEmail()).matches()) {
                throw new ValidationException("Invalid email format");
            }
            existingVendor.setEmail(vendor.getEmail());
        }

        return vendorRepository.save(existingVendor);
    }

    public void deleteVendorById(UUID id) {
        Vendor existingVendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor with ID " + id + " not found"));

        vendorRepository.delete(existingVendor);
    }
}
