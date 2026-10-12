package com.api.springboot.config;

import com.api.spring.di.entity.Product;
import com.api.spring.di.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoDataConfig {

    @Bean
    public CommandLineRunner seedProducts(ProductRepository repository) {
        return args -> repository.save(
                new Product(1L, "키보드", 50000)
        );
    }
}