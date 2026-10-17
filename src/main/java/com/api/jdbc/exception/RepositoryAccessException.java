package com.api.jdbc.exception;

public class RepositoryAccessException extends RuntimeException {
    public RepositoryAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
