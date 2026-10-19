package com.api.springboot.service;

import com.api.spring.di.entity.Product;
import com.api.spring.di.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductBatchService {
    private final ProductRepository productRepository;

    public ProductBatchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public void saveTwoWithoutTransaction(Product first, Product second) {
        // 두 상품을 순서대로 저장
        productRepository.save(first);
        productRepository.save(second);
    }

    @Transactional
    public void saveTwo(Product first, Product second) {
        // 두 상품을 순서대로 저장
        productRepository.save(first);
        productRepository.save(second);
    }
}
