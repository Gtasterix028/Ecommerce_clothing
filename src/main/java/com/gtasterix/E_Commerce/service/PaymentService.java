package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.dto.PaymentDTO;
import com.gtasterix.E_Commerce.exception.PaymentNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.mapper.PaymentMapper;
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

    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Order order = orderRepository.findById(paymentDTO.getOrderID())
                .orElseThrow(() -> new ValidationException("Order with ID " + paymentDTO.getOrderID() + " not found"));

        Payment payment = PaymentMapper.toEntity(paymentDTO, order);
        validatePayment(payment);
        Payment savedPayment = paymentRepository.save(payment);
        return PaymentMapper.toDTO(savedPayment);
    }

    public PaymentDTO getPaymentById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with ID " + id + " not found"));
        return PaymentMapper.toDTO(payment);
    }

    public PaymentDTO updatePayment(UUID id, PaymentDTO paymentDTO) {
        if (!paymentRepository.existsById(id)) {
            throw new PaymentNotFoundException("Payment with ID " + id + " not found");
        }

        Order order = orderRepository.findById(paymentDTO.getOrderID())
                .orElseThrow(() -> new ValidationException("Order with ID " + paymentDTO.getOrderID() + " not found"));

        Payment payment = PaymentMapper.toEntity(paymentDTO, order);
        payment.setPaymentID(id);
        Payment updatedPayment = paymentRepository.save(payment);
        return PaymentMapper.toDTO(updatedPayment);
    }

    public List<PaymentDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream().map(PaymentMapper::toDTO).toList();
    }

    public PaymentDTO patchPaymentById(UUID id, PaymentDTO paymentDTO) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with ID " + id + " not found"));

        if (paymentDTO.getOrderID() != null) {
            Order order = orderRepository.findById(paymentDTO.getOrderID())
                    .orElseThrow(() -> new ValidationException("Order with ID " + paymentDTO.getOrderID() + " not found"));
            existingPayment.setOrder(order);
        }
        if (paymentDTO.getPaymentDate() != null) existingPayment.setPaymentDate(paymentDTO.getPaymentDate());
        if (paymentDTO.getAmount() != null) {
            if (paymentDTO.getAmount() <= 0) {
                throw new ValidationException("Amount must be greater than zero");
            }
            existingPayment.setAmount(paymentDTO.getAmount());
        }
        if (paymentDTO.getPaymentMethod() != null) existingPayment.setPaymentMethod(paymentDTO.getPaymentMethod());
        if (paymentDTO.getPaymentStatus() != null) existingPayment.setPaymentStatus(paymentDTO.getPaymentStatus());

        Payment updatedPayment = paymentRepository.save(existingPayment);
        return PaymentMapper.toDTO(updatedPayment);
    }

    public void deletePaymentById(UUID id) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with ID " + id + " not found"));
        paymentRepository.delete(existingPayment);
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
}
