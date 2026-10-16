package com.api.spring.scan.service;

import com.api.spring.di.entity.Product;
import com.api.spring.di.exception.DuplicateProductException;
import com.api.spring.di.exception.ProductNotFoundException;
import com.api.spring.di.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("상품은 null일 수 없습니다.");
        }

        productRepository.save(product);
    }

    public Product getProduct(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("조회 ID : " + id + " 상품이 존재하지 않습니다."));
    }

    public Product createProduct(long id, String name, int price) {
        if (productRepository.findById(id).isPresent()) {
            throw new DuplicateProductException("이미 상품이 존재합니다.");
        }

        Product product = new Product(id, name, price);
        productRepository.save(product);

        return product;
    }
}
