package com.api.springboot.service;

import com.api.springboot.entity.ProductEntity;
import com.api.springboot.exception.ProductNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JpaProductPriceService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void changePrice(Long id, int newPrice) {
        ProductEntity product = entityManager.find(ProductEntity.class, id);
        if (product == null) {
            throw new ProductNotFoundException("상품을 찾을 수 없습니다.");
        }
        product.changePrice(newPrice);
    }

    @Transactional
    public void changePriceThenFlushAndFail(Long id, int newPrice) {
        ProductEntity product = entityManager.find(ProductEntity.class, id);
        if (product == null) {
            throw new ProductNotFoundException("상품을 찾을 수 없습니다.");
        }
        product.changePrice(newPrice);
        entityManager.flush();
        throw new IllegalStateException();
    }
}
