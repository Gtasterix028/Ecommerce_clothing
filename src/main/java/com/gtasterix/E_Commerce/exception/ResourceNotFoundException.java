package com.gtasterix.E_Commerce.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String userNotFound) {
        super(userNotFound);
    }
}
