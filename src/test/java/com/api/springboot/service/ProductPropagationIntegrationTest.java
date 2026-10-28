package com.api.springboot.service;

import com.api.spring.di.entity.Product;
import com.api.spring.di.repository.ProductRepository;
import com.api.springboot.BackendStudyApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.UnexpectedRollbackException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BackendStudyApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductPropagationIntegrationTest {
    @Autowired
    private ProductPropagationService productPropagationService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void 저장_후_두_상품이_존재하고_일치하는지_확인한다() {
        Product first = new Product(100L, "keyboard", 1000);
        Product second = new Product(200L, "mouse", 2000);

        productPropagationService.saveTwo(first, second);

        Optional<Product> result_first = productRepository.findById(100L);
        Optional<Product> result_second = productRepository.findById(200L);

        assertTrue(result_first.isPresent());
        assertTrue(result_second.isPresent());

        assertEquals("keyboard", result_first.get().getName());
        assertEquals(1000, result_first.get().getPrice());

        assertEquals("mouse", result_second.get().getName());
        assertEquals(2000, result_second.get().getPrice());
    }

    @Test
    public void 저장_후_바깥_메서드에서_예외가_발생하여_두_상품이_롤백된다() {
        Product first = new Product(100L, "keyboard", 1000);
        Product second = new Product(200L, "mouse", 2000);

        assertThrows(IllegalStateException.class, () ->
                productPropagationService.saveTwoThenFail(first, second));

        Optional<Product> result_first = productRepository.findById(100L);
        Optional<Product> result_second = productRepository.findById(200L);

        assertFalse(result_first.isPresent());
        assertFalse(result_second.isPresent());
    }

    @Test
    public void 저장_후_안쪽_메서드에서_예외가_발생하여_두_상품이_롤백된다() {
        Product first = new Product(100L, "keyboard", 1000);
        Product second = new Product(200L, "mouse", 2000);

        assertThrows(IllegalStateException.class, () ->
                productPropagationService.saveTwoInnerFailure(first, second));

        Optional<Product> result_first = productRepository.findById(100L);
        Optional<Product> result_second = productRepository.findById(200L);

        assertFalse(result_first.isPresent());
        assertFalse(result_second.isPresent());
    }

    @Test
    public void 저장_중_안쪽_메서드에서_예외가_발생하여_rollbackonly가_설정되어_롤백된다() {
        Product first = new Product(100L, "keyboard", 1000);
        Product second = new Product(200L, "mouse", 2000);

        assertThrows(UnexpectedRollbackException.class, () ->
                productPropagationService.saveTwoAndCatchInnerFailure(first, second));

        Optional<Product> result_first = productRepository.findById(100L);
        Optional<Product> result_second = productRepository.findById(200L);

        assertFalse(result_first.isPresent());
        assertFalse(result_second.isPresent());
    }

    @Test
    public void 두_상품_저장_후_바깥_메서드에서_예외_발생_후_false를_반환하여_커밋된다() {
        Product first = new Product(100L, "keyboard", 1000);
        Product second = new Product(200L, "mouse", 2000);

        productPropagationService.saveTwoAndCatchLocalFailure(first, second);

        Optional<Product> result_first = productRepository.findById(100L);
        Optional<Product> result_second = productRepository.findById(200L);

        assertTrue(result_first.isPresent());
        assertTrue(result_second.isPresent());

        assertEquals(100L, result_first.get().getId());
        assertEquals("keyboard", result_first.get().getName());
        assertEquals(1000, result_first.get().getPrice());

        assertEquals(200L, result_second.get().getId());
        assertEquals("mouse", result_second.get().getName());
        assertEquals(2000, result_second.get().getPrice());
    }

    @Test
    public void 바깥_트랜잭션은_롤백된다() {
        Product first = new Product(100L, "keyboard", 1000);
        Product second = new Product(200L, "mouse", 2000);

        assertThrows(IllegalStateException.class, () ->
                productPropagationService.saveWithNewTransactionThenFail(first, second));

        Optional<Product> result_first = productRepository.findById(100L);
        Optional<Product> result_second = productRepository.findById(200L);

        assertFalse(result_first.isPresent());
        assertTrue(result_second.isPresent());

        assertEquals(200L, result_second.get().getId());
        assertEquals("mouse", result_second.get().getName());
        assertEquals(2000, result_second.get().getPrice());
    }

    @Test
    public void 안쪽_트랜잭션은_롤백된다() {
        Product first = new Product(100L, "keyboard", 1000);
        Product second = new Product(200L, "mouse", 2000);

        boolean success = productPropagationService.saveAndCatchNewTransactionFailure(first, second);

        assertFalse(success);

        Optional<Product> result_first = productRepository.findById(100L);
        Optional<Product> result_second = productRepository.findById(200L);

        assertTrue(result_first.isPresent());
        assertFalse(result_second.isPresent());

        assertEquals(100L, result_first.get().getId());
        assertEquals("keyboard", result_first.get().getName());
        assertEquals(1000, result_first.get().getPrice());
    }

    @Test
    public void 두_트랜잭션_모두_롤백된다() {
        Product first = new Product(100L, "keyboard", 1000);
        Product second = new Product(200L, "mouse", 2000);

        assertThrows(IllegalStateException.class, () ->
                productPropagationService.saveWithNewTransactionFailure(first, second));

        Optional<Product> result_first = productRepository.findById(100L);
        Optional<Product> result_second = productRepository.findById(200L);

        assertFalse(result_first.isPresent());
        assertFalse(result_second.isPresent());
    }
}
