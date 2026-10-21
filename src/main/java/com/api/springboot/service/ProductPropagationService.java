package com.api.springboot.service;

import com.api.spring.di.entity.Product;
import com.api.spring.di.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductPropagationService {
    private final ProductRepository productRepository;
    private final ProductSaveService productSaveService;

    public ProductPropagationService(ProductRepository productRepository, ProductSaveService productSaveService) {
        this.productRepository = productRepository;
        this.productSaveService = productSaveService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveTwo(Product first, Product second) {
        productRepository.save(first);
        productSaveService.save(second);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveTwoThenFail(Product first, Product second) {
        productRepository.save(first);
        productSaveService.save(second);
        throw new IllegalStateException();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveTwoInnerFailure(Product first, Product second) {
        productRepository.save(first);
        productSaveService.saveThenFail(second);
    }
}
