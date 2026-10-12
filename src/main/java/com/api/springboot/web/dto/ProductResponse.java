package com.api.springboot.web.dto;

import com.api.spring.di.entity.Product;

public record ProductResponse(
        Long id,
        String name,
        int price
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }
}
