package com.gtasterix.E_Commerce.exception;

public class OrderItemNotFoundException extends RuntimeException{
    public OrderItemNotFoundException(String s) {
        super(s);
    }
}
