package com.api.springboot.web.controller;

import com.api.spring.di.entity.Product;
import com.api.spring.scan.service.ProductService;
import com.api.springboot.web.dto.ProductCreateRequest;
import com.api.springboot.web.dto.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("{productId}")
    public ProductResponse getProduct(
            @PathVariable Long productId
    ) {
        return ProductResponse.from(productService.getProduct(productId));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductCreateRequest request
    ) {
        Product result = productService.createProduct(request.id(), request.name(), request.price());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/products/" + result.getId()))
                .body(ProductResponse.from(result));
    }
}
