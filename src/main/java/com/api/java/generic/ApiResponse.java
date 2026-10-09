package com.api.java.generic;

public class ApiResponse<T> {
    private final String message;
    private final int httpStatus;
    private final T data;

    public ApiResponse(String message, int httpStatus, T data) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }
    public int getHttpStatus() {
        return httpStatus;
    }
    public T getData() {
        return data;
    }

    public static <D> ApiResponse<D> success(String message, D data) {
        return new ApiResponse<>(
                message,
                200,
                data
        );
    }
}
