package com.api.spring.di.repository;

import com.api.spring.di.entity.Product;

import java.util.Optional;

public interface ProductRepository {
    void save(Product product);
    Optional<Product> findById(long id);
}
