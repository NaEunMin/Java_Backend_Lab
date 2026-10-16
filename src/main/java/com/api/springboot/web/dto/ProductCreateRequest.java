package com.api.springboot.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductCreateRequest(

        @NotNull
        @Positive
        Long id,

        @NotBlank
        String name,

        @NotNull
        @Positive
        Integer price
) {
}
