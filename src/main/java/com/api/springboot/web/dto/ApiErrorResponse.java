package com.api.springboot.web.dto;

public record ApiErrorResponse(
        String code,
        String message
) {
}
