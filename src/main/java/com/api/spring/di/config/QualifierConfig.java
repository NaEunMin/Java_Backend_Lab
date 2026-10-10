package com.api.spring.di.config;

import com.api.spring.di.service.ProductService;
import com.api.spring.di.repository.InMemoryProductRepository;
import com.api.spring.di.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class QualifierConfig {

    @Bean
    @Primary
    public ProductRepository mainRepository() {
        return new InMemoryProductRepository();
    }

    @Bean
    public ProductRepository backupRepository() {
        return new InMemoryProductRepository();
    }

    @Bean
    public ProductService productService(
            @Qualifier("backupRepository") ProductRepository productRepository) {
        return new ProductService(productRepository);
    }
}
