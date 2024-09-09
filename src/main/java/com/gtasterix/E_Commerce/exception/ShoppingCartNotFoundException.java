package com.gtasterix.E_Commerce.exception;

public class ShoppingCartNotFoundException extends RuntimeException{
    public ShoppingCartNotFoundException(String s) {
        super(s);
    }
}
