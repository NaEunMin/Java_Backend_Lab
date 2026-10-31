package com.api.springboot.service;

import com.api.spring.di.entity.Product;
import com.api.spring.di.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;

@Service
public class ReadOnlyTransactionService {
    private final ProductRepository productRepository;

    public ReadOnlyTransactionService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public boolean isReadOnlyTransaction() {
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly();
    }

    @Transactional(readOnly = true)
    public void saveInReadOnly(Product product) {
        productRepository.save(product);
    }
}
