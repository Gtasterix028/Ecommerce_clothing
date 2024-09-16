package com.gtasterix.E_Commerce.controller;

import com.gtasterix.E_Commerce.Util.Response;
import com.gtasterix.E_Commerce.dto.PaymentDTO;
import com.gtasterix.E_Commerce.exception.PaymentNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Response> createPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        try {
            PaymentDTO createdPayment = paymentService.createPayment(paymentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Payment created successfully", createdPayment, false));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "Validation error", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Unexpected error occurred", e.getMessage(), true));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getPaymentById(@PathVariable UUID id) {
        try {
            PaymentDTO paymentDTO = paymentService.getPaymentById(id);
            return ResponseEntity.ok(new Response("Payment retrieved successfully", paymentDTO, false));
        } catch (PaymentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage(), "Payment not found", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Unexpected error occurred", e.getMessage(), true));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updatePayment(@PathVariable UUID id, @Valid @RequestBody PaymentDTO paymentDTO) {
        try {
            PaymentDTO updatedPayment = paymentService.updatePayment(id, paymentDTO);
            return ResponseEntity.ok(new Response("Payment updated successfully", updatedPayment, false));
        } catch (PaymentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage(), "Payment not found", true));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "Validation error", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Unexpected error occurred", e.getMessage(), true));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deletePaymentById(@PathVariable UUID id) {
        try {
            paymentService.deletePaymentById(id);
            return ResponseEntity.ok(new Response("Payment deleted successfully", "Payment erased", false));
        } catch (PaymentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage(), "Payment not found", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Unexpected error occurred", e.getMessage(), true));
        }
    }

    @GetMapping
    public ResponseEntity<Response> getAllPayments() {
        try {
            List<PaymentDTO> payments = paymentService.getAllPayments();
            return ResponseEntity.ok(new Response("Payments retrieved successfully", payments, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Unexpected error occurred", e.getMessage(), true));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> patchPaymentById(@PathVariable UUID id, @RequestBody PaymentDTO paymentDTO) {
        try {
            PaymentDTO updatedPayment = paymentService.patchPaymentById(id, paymentDTO);
            return ResponseEntity.ok(new Response("Payment updated successfully", updatedPayment, false));
        } catch (PaymentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage(), "Payment not found", true));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage(), "Validation error", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Unexpected error occurred", e.getMessage(), true));
        }
    }
}
