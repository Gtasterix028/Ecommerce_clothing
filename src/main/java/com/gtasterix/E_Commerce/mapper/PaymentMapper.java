package com.gtasterix.E_Commerce.mapper;

import com.gtasterix.E_Commerce.dto.PaymentDTO;
import com.gtasterix.E_Commerce.model.Payment;
import com.gtasterix.E_Commerce.model.Order;

public class PaymentMapper {

    public static PaymentDTO toDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentID(payment.getPaymentID());
        dto.setOrderID(payment.getOrder().getOrderID());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentStatus(payment.getPaymentStatus());
        return dto;
    }

    public static Payment toEntity(PaymentDTO dto, Order order) {
        Payment payment = new Payment();
        payment.setPaymentID(dto.getPaymentID());
        payment.setOrder(order);
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentStatus(dto.getPaymentStatus());
        return payment;
    }
}
