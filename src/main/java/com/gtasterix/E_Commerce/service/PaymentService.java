package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.exception.PaymentNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.model.Order;
import com.gtasterix.E_Commerce.model.Payment;
import com.gtasterix.E_Commerce.repository.OrderRepository;
import com.gtasterix.E_Commerce.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Payment createPayment(Payment payment) {
        try {
            validatePayment(payment);
            Order order = orderRepository.findById(payment.getOrder().getOrderID())
                    .orElseThrow(() -> new ValidationException("Oder with ID " + payment.getOrder().getOrderID() + " not found"));
            payment.setOrder(order);
            return paymentRepository.save(payment);
        }catch (ValidationException e){
            throw new ValidationException("Failed to create payement "+e.getMessage());
        }catch (Exception e){
            throw new RuntimeException("Unexpected error occurred while creating payment: "+e.getMessage());
        }
    }

    public Payment getPaymentById(UUID id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with ID " + id + " not found"));
    }

    public Payment updatePayment(UUID id, Payment payment) {
        validatePayment(payment);
        if (!paymentRepository.existsById(id)) {
            throw new PaymentNotFoundException("Payment with ID " + id + " not found");
        }
        payment.setPaymentID(id);
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    private void validatePayment(Payment payment) {
        if (payment.getOrder() == null) {
            throw new ValidationException("Order cannot be null");
        }
        if (payment.getPaymentDate() == null) {
            throw new ValidationException("Payment date cannot be null");
        }
        if (payment.getAmount() == null || payment.getAmount() <= 0) {
            throw new ValidationException("Amount must be greater than zero");
        }
        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().isEmpty()) {
            throw new ValidationException("Payment method cannot be null or empty");
        }
        if (payment.getPaymentStatus() == null) {
            throw new ValidationException("Payment status cannot be null");
        }
    }

    public Payment patchPaymentById(UUID id, Payment payment) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with ID " + id + " not found"));

        if (payment.getOrder() != null) existingPayment.setOrder(payment.getOrder());
        if (payment.getPaymentDate() != null) existingPayment.setPaymentDate(payment.getPaymentDate());
        if (payment.getAmount() != null) {
            if (payment.getAmount() <= 0) {
                throw new ValidationException("Amount must be greater than zero");
            }
            existingPayment.setAmount(payment.getAmount());
        }
        if (payment.getPaymentMethod() != null) existingPayment.setPaymentMethod(payment.getPaymentMethod());
        if (payment.getPaymentStatus() != null) existingPayment.setPaymentStatus(payment.getPaymentStatus());

        return paymentRepository.save(existingPayment);
    }

    public void deletePaymentById(UUID id) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with ID " + id + " not found"));

        paymentRepository.delete(existingPayment);
    }
}
