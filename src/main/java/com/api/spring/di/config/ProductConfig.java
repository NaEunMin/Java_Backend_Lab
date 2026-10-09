package com.api.spring.di.config;

import com.api.spring.di.ProductService;
import com.api.spring.di.repository.InMemoryProductRepository;
import com.api.spring.di.repository.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfig {

    @Bean
    public ProductRepository productRepository() {
        return new InMemoryProductRepository();
    }

    @Bean
    public ProductService productService(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }
}
