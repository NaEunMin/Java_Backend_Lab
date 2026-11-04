package com.api.springboot.service;

import com.api.springboot.entity.ProductEntity;
import com.api.springboot.exception.ProductNotFoundException;
import com.api.springboot.repository.JpaProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JpaProductPriceService {

    private final JpaProductRepository repository;
    public JpaProductPriceService(JpaProductRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void changePrice(Long id, int newPrice) {
        ProductEntity product = repository.findById(id)
                        .orElseThrow(()-> new ProductNotFoundException("상품을 찾을 수 없습니다."));
        product.changePrice(newPrice);
    }

    @Transactional
    public void changePriceThenFlushAndFail(Long id, int newPrice) {
        ProductEntity product = repository.findById(id)
                        .orElseThrow(()-> new ProductNotFoundException("상품을 찾을 수 없습니다."));
        product.changePrice(newPrice);
        repository.flush();
        throw new IllegalStateException();
    }
}
