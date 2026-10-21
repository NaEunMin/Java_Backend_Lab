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

    @Transactional
    public boolean saveAndCatchRuntimeException(Product product) {
        //예외를 잡은 뒤 false 반환 -> 서비스 밖으로 예외를 전파하지 않음 -> 언체크 예외를 Spring의 트랙잭션 프록시가 받지 못하므로 롤백 X
        try {
            productRepository.save(product);
            throw new IllegalStateException("후속 처리 실패");
        } catch (IllegalStateException e) {
            return false;
        }
    }

    @Transactional
    public void saveAndRethrowRuntimeException(Product product) {
        //예외를 잡은 뒤 예외를 다시 던지기 -> 서비스 밖으로 예외 전파 -> 언체크 예외를 Spring의 트랜잭션 프록시가 받아서 롤백 O
        try {
            productRepository.save(product);
            throw new IllegalStateException("후속 처리 실패");
        } catch (IllegalStateException e) {
            throw e;
        }
    }

    public void callInternallyWithoutTransaction(Product product) {
        // 기존 saveThenFailWithRuntimeException(product)를 호출
        saveThenFailWithRuntimeException(product);
    }

    @Transactional
    public void callInternallyWithTransaction(Product product) {
        // 기존 saveThenFailWithRuntimeException(product)를 호출
        saveThenFailWithRuntimeException(product);
    }
}
