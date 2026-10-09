package com.api.spring.di.repository;

import com.api.spring.di.entity.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryProductRepository implements ProductRepository {
    private final Map<Long, Product> repository;

    public InMemoryProductRepository() {
        repository = new HashMap<>();
    }

    @Override
    public void save(Product product) {
        repository.put(product.getId(), product);
    }

    @Override
    public Optional<Product> findById(long id) {
        Product product = repository.get(id);
        return Optional.ofNullable(product);
    }
}
