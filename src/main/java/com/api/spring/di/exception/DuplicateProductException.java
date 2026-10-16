package com.api.spring.di.exception;

public class DuplicateProductException extends RuntimeException {
    public DuplicateProductException(String message) {
        super(message);
    }
}
