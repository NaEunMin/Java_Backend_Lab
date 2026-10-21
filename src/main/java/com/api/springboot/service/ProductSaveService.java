package com.api.springboot.service;

import com.api.spring.di.entity.Product;
import com.api.spring.di.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductSaveService {
    private final ProductRepository productRepository;
    public ProductSaveService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Product product) {
        productRepository.save(product);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveThenFail(Product product) {
        productRepository.save(product);
        throw new IllegalStateException();
    }
}
