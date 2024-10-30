package com.gtasterix.E_Commerce.controller;

import com.gtasterix.E_Commerce.Util.Response;
import com.gtasterix.E_Commerce.dto.VariantDTO;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/variants")
public class VariantController {

    @Autowired
    private VariantService variantService;

    @PostMapping
    public ResponseEntity<Response> createVariant(@RequestBody VariantDTO variantDTO) {
        try {
            VariantDTO createdVariant = variantService.createVariant(variantDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Variant created successfully", createdVariant, false));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getVariantById(@PathVariable UUID id) {
        try {
            VariantDTO variant = variantService.getVariantById(id);
            return ResponseEntity.ok(new Response("Variant retrieved successfully", variant, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Variant with ID " + id + " not found", e.getMessage(), true));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateVariant(@PathVariable UUID id, @RequestBody VariantDTO variantDTO) {
        try {
            VariantDTO updatedVariant = variantService.updateVariant(id, variantDTO);
            return ResponseEntity.ok(new Response("Variant updated successfully", updatedVariant, false));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> patchVariantById(@PathVariable UUID id, @RequestBody VariantDTO variantDTO) {
        try {
            VariantDTO updatedVariant = variantService.patchVariantById(id, variantDTO);
            return ResponseEntity.ok(new Response("Variant updated successfully", updatedVariant, false));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteVariantById(@PathVariable UUID id) {
        try {
            variantService.deleteVariantById(id);
            return ResponseEntity.ok(new Response("Variant deleted successfully", "Variant erased", false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Variant with ID " + id + " not found", e.getMessage(), true));
        }
    }

    @GetMapping
    public ResponseEntity<Response> getAllVariants() {
        try {
            List<VariantDTO> variants = variantService.getAllVariants();
            return ResponseEntity.ok(new Response("Variants retrieved successfully", variants, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("An error occurred", e.getMessage(), true));
        }
    }
}
