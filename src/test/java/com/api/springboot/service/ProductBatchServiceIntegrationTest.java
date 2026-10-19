package com.api.springboot.service;

import com.api.spring.di.entity.Product;
import com.api.spring.di.repository.ProductRepository;
import com.api.springboot.BackendStudyApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BackendStudyApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductBatchServiceIntegrationTest {

    @Autowired //Spring이 제공하는 트랜잭션 기능이 필요해서 Autowired로 주입
    private ProductBatchService productBatchService;

    @Autowired //서비스가 사용하는 DB에 데이터를 준비하고 결과를 확인하기 위해 Autowired로 주입
    private ProductRepository productRepository;

    @Test
    public void 모두_성공하면_두_상품이_저장된다() {
        Product first = new Product(100L, "keyboard", 1000);
        Product second = new Product(200L, "mouse", 2000);

        productBatchService.saveTwo(first, second);

        Optional<Product> first_result = productRepository.findById(100L);
        Optional<Product> second_result = productRepository.findById(200L);

        assertTrue(first_result.isPresent());
        assertTrue(second_result.isPresent());

        assertEquals(100L, first_result.get().getId());
        assertEquals(200L, second_result.get().getId());

        assertEquals("keyboard", first_result.get().getName());
        assertEquals("mouse", second_result.get().getName());

        assertEquals(1000, first_result.get().getPrice());
        assertEquals(2000, second_result.get().getPrice());
    }

    @Test
    public void 트랜잭션이_없으면_첫_번째_저장은_남는다() {
        productRepository.save(new Product(200L, "mouse", 2000));

        Product first = new Product(100L, "keyboard", 1000);
        Product second = new Product(200L, "monitor", 3000);

        assertThrows(DuplicateKeyException.class, () ->
                productBatchService.saveTwoWithoutTransaction(first, second));

        Optional<Product> first_result = productRepository.findById(100L);
        Optional<Product> before_second_result = productRepository.findById(200L);

        assertTrue(first_result.isPresent());
        assertTrue(before_second_result.isPresent());

        assertEquals(100L, first_result.get().getId());
        assertEquals("keyboard", first_result.get().getName());
        assertEquals(1000, first_result.get().getPrice());

        assertEquals(200L, before_second_result.get().getId());
        assertEquals("mouse", before_second_result.get().getName());
        assertEquals(2000, before_second_result.get().getPrice());
    }

    @Test
    public void 트랜잭션이_있으면_첫_번째_저장도_취소된다() {
        productRepository.save(new Product(200L, "mouse", 2000));

        Product first = new Product(100L, "keyboard", 1000);
        Product second = new Product(200L, "monitor", 3000);

        assertThrows(DuplicateKeyException.class, () ->
                productBatchService.saveTwo(first, second));

        Optional<Product> first_result = productRepository.findById(100L);
        Optional<Product> before_second_result = productRepository.findById(200L);

        assertFalse(first_result.isPresent());
        assertTrue(before_second_result.isPresent());

        assertEquals(200L, before_second_result.get().getId());
        assertEquals("mouse", before_second_result.get().getName());
        assertEquals(2000, before_second_result.get().getPrice());
    }
}
