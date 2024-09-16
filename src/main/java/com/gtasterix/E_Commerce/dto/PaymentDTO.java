package com.gtasterix.E_Commerce.dto;

import com.gtasterix.E_Commerce.model.PaymentStatus;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentDTO {

    private UUID paymentID;

    @NotNull(message = "Order ID cannot be null")
    private UUID orderID;

    @NotNull(message = "Payment date cannot be null")
    private LocalDateTime paymentDate;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private Double amount;

    @NotBlank(message = "Payment method cannot be blank")
    private String paymentMethod;

    @NotNull(message = "Payment status cannot be null")
    private PaymentStatus paymentStatus;
}
