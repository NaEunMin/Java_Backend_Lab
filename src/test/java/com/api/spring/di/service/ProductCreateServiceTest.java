package com.api.spring.di.service;

import com.api.spring.di.entity.Product;
import com.api.spring.di.exception.DuplicateProductException;
import com.api.spring.scan.repository.InMemoryProductRepository;
import com.api.spring.scan.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;

public class ProductCreateServiceTest {

    @InjectMocks
    private final ProductService productService = new ProductService(new InMemoryProductRepository());

    @Test
    public void 상품을_정상적으로_등록한다() {
        //when & then
        Product result = productService.createProduct(1L, "keyboard", 3000);

        assertEquals(1L, result.getId());
        assertEquals("keyboard", result.getName());
        assertEquals(3000, result.getPrice());
    }

    @Test
    public void 중복된_상품을_저장하면_예외를_반환한다() {
        Product product = productService.createProduct(1L, "keyboard", 3000);

        assertThrows(DuplicateProductException.class, () ->
                productService.createProduct(1L, "book", 2000)
        );

        Product result = productService.getProduct(1L);

        assertEquals(1L, result.getId());
        assertEquals("keyboard", result.getName());
        assertEquals(3000, result.getPrice());
    }
}
