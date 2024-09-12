package com.gtasterix.E_Commerce.controller;

import com.gtasterix.E_Commerce.Util.Response;
import com.gtasterix.E_Commerce.dto.VendorDTO;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.exception.VendorNotFoundException;
import com.gtasterix.E_Commerce.mapper.VendorMapper;
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
            Vendor vendor = VendorMapper.toEntity(vendorDTO);
            Vendor createdVendor = vendorService.createVendor(vendorDTO);
            VendorDTO createdVendorDTO = VendorMapper.toDTO(createdVendor);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Vendor created successfully", createdVendorDTO, false));
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
            VendorDTO vendorDTO = VendorMapper.toDTO(vendor);
            return ResponseEntity.ok(new Response("Vendor retrieved successfully", vendorDTO, false));
        } catch (VendorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateVendor(@PathVariable UUID id, @RequestBody VendorDTO vendorDTO) {
        try {
            Vendor vendor = VendorMapper.toEntity(vendorDTO);
            Vendor updatedVendor = vendorService.updateVendor(id, vendorDTO);
            VendorDTO updatedVendorDTO = VendorMapper.toDTO(updatedVendor);
            return ResponseEntity.ok(new Response("Vendor updated successfully", updatedVendorDTO, false));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (VendorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteVendorById(@PathVariable UUID id) {
        try {
            vendorService.deleteVendorById(id);
            return ResponseEntity.ok(new Response("Vendor deleted successfully", "Vendor erased", false));
        } catch (VendorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @GetMapping
    public ResponseEntity<Response> getAllVendors() {
        try {
            List<Vendor> vendors = vendorService.getAllVendors();
            List<VendorDTO> vendorDTOs = vendors.stream()
                    .map(VendorMapper::toDTO)
                    .toList();
            return ResponseEntity.ok(new Response("Vendors retrieved successfully", vendorDTOs, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> patchVendorById(@PathVariable UUID id, @RequestBody VendorDTO vendorDTO) {
        try {
            Vendor vendor = VendorMapper.toEntity(vendorDTO);
            Vendor updatedVendor = vendorService.patchVendorById(id, vendorDTO);
            VendorDTO updatedVendorDTO = VendorMapper.toDTO(updatedVendor);
            return ResponseEntity.ok(new Response("Vendor updated successfully", updatedVendorDTO, false));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (VendorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }
}
