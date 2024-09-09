package com.gtasterix.E_Commerce.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String emailAlreadyExists) {
        super(emailAlreadyExists);
    }
}
