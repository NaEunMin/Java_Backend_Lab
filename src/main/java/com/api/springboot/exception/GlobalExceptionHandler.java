package com.api.springboot.exception;

import com.api.spring.di.exception.DuplicateProductException;
import com.api.spring.di.exception.ProductNotFoundException;
import com.api.springboot.web.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleProductNotFound(
            ProductNotFoundException exception
    ) {
        ApiErrorResponse response = new ApiErrorResponse("PRODUCT NOT FOUND", "상품을 찾지 못했습니다.");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentTypeMismatchException exception
    ) {
        ApiErrorResponse response = new ApiErrorResponse("INVALID PARAMETER TYPE", "요청 값이 형식과 올바르지 않습니다.");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(DuplicateProductException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateProduct(
            DuplicateProductException exception
    ) {
        ApiErrorResponse response = new ApiErrorResponse("PRODUCT_ALREADY_EXISTS", "이미 존재하는 상품입니다.");
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception
    ) {
        ApiErrorResponse response = new ApiErrorResponse("INVALID_REQUEST_BODY", "요청 본문의 형식이 올바르지 않습니다.");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception
    ) {
        ApiErrorResponse response = new ApiErrorResponse("INVALID_REQUEST", "요청 값이 조건을 만족하지 않습니다.");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
