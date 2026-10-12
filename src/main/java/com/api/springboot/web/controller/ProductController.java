package com.api.springboot.web.controller;

import com.api.spring.scan.service.ProductService;
import com.api.springboot.web.dto.ProductResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    ){
        return ProductResponse.from(productService.getProduct(productId));
    }
}
