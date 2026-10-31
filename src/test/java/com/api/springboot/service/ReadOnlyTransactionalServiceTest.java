package com.api.springboot.service;

import com.api.spring.di.entity.Product;
import com.api.spring.di.repository.ProductRepository;
import com.api.springboot.BackendStudyApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BackendStudyApplication.class)
public class ReadOnlyTransactionalServiceTest {

    @Autowired
    private ReadOnlyTransactionService readOnlyTransactionService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.update("delete from products");
    }

    @Test
    public void 읽기_전용_트랜잭션으로_상품을_조회한다() {
        Product product = new Product(100L, "keyboard", 1000);
        productRepository.save(product);

        Optional<Product> result = readOnlyTransactionService.findById(100L);

        assertTrue(result.isPresent());
        assertEquals(100L, result.get().getId());
        assertEquals("keyboard", result.get().getName());
        assertEquals(1000, result.get().getPrice());
    }

    @Test
    public void 읽기_전용_설정을_확인한다() {
        boolean success = readOnlyTransactionService.isReadOnlyTransaction();
        assertTrue(success);
    }

    @Test
    public void mysql_읽기_전용_트랜잭션에서_쓰기를_시도한다() {
        Product product = new Product(200L, "mouse", 2000);

        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                readOnlyTransactionService.saveInReadOnly(product));

        assertTrue(productRepository.findById(200L).isEmpty());
        assertEquals("Connection is read-only. Queries leading to data modification are not allowed", exception.getMostSpecificCause().getMessage());
    }

}
