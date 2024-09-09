package com.gtasterix.E_Commerce.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String s) {
        super(s);
    }
}
