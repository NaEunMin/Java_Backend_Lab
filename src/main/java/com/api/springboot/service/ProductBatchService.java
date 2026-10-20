package com.api.springboot.service;

import com.api.spring.di.entity.Product;
import com.api.spring.di.repository.ProductRepository;
import com.api.springboot.exception.ProductProcessingException;
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

    @Transactional
    public void saveThenFailWithRuntimeException(Product product) {
        //저장 후 런타임 예외 발생
        productRepository.save(product);
        throw new IllegalStateException();
    }

    @Transactional
    public void saveThenFailWithCheckedException(Product product) throws ProductProcessingException {
        //저장 후 체크 예외 발생
        productRepository.save(product);
        throw new ProductProcessingException("");
    }

    @Transactional(rollbackFor = ProductProcessingException.class)
    public void saveThenFailWithCheckedExceptionAndRollback(Product product) throws ProductProcessingException {
        //같은 체크에 롤백 규칙 적용
        productRepository.save(product);
        throw new ProductProcessingException("");
    }
}
