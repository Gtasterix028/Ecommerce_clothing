package com.gtasterix.E_Commerce.controller;

import com.gtasterix.E_Commerce.Util.Response;
import com.gtasterix.E_Commerce.dto.VendorDTO;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.model.Vendor;
import com.gtasterix.E_Commerce.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping
    public ResponseEntity<Response> createVendor(@RequestBody VendorDTO vendorDTO) {
        try {
            Vendor createdVendor = vendorService.createVendor(vendorDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Vendor created successfully", createdVendor, false));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getVendorById(@PathVariable UUID id) {
        try {
            Vendor vendor = vendorService.getVendorById(id);
            return ResponseEntity.ok(new Response("Vendor retrieved successfully", vendor, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Vendor with ID " + id + " not found", e.getMessage(), true));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateVendor(@PathVariable UUID id, @RequestBody VendorDTO vendorDTO) {
        try {
            Vendor updatedVendor = vendorService.updateVendor(id, vendorDTO);
            return ResponseEntity.ok(new Response("Vendor updated successfully", updatedVendor, false));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> patchVendorById(@PathVariable UUID id, @RequestBody VendorDTO vendorDTO) {
        try {
            Vendor updatedVendor = vendorService.patchVendorById(id, vendorDTO);
            return ResponseEntity.ok(new Response("Vendor updated successfully", updatedVendor, false));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteVendorById(@PathVariable UUID id) {
        try {
            vendorService.deleteVendorById(id);
            return ResponseEntity.ok(new Response("Vendor deleted successfully", "Vendor erased", false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Vendor with ID " + id + " not found", e.getMessage(), true));
        }
    }

    @GetMapping
    public ResponseEntity<Response> getAllVendors() {
        try {
            List<Vendor> vendors = vendorService.getAllVendors();
            return ResponseEntity.ok(new Response("Vendors retrieved successfully", vendors, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }
}
