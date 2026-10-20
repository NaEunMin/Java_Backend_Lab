package com.api.springboot.exception;

public class ProductProcessingException extends Exception {
    public ProductProcessingException(String message) {
        super(message);
    }
}
