package com.api.springboot.jpa;

import com.api.spring.di.exception.ProductNotFoundException;
import com.api.springboot.BackendStudyApplication;
import com.api.springboot.entity.ProductEntity;
import com.api.springboot.repository.JpaProductRepository;
import com.api.springboot.service.JpaProductPriceService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BackendStudyApplication.class)
@ActiveProfiles("h2")
@Transactional
public class JpaProductRepositoryTest {

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Autowired
    private JpaProductPriceService jpaProductPriceService;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void 신규_상품을_저장하고_조회한다() {
        ProductEntity product = new ProductEntity(100L, "keyboard", 1000);
        jpaProductRepository.save(product);
        jpaProductRepository.flush();

        entityManager.clear();

        ProductEntity result = jpaProductRepository.findById(100L).orElse(null);
        assertNotNull(result);

        assertEquals(100L, result.getId());
        assertEquals("keyboard", result.getName());
        assertEquals(1000, result.getPrice());
    }

    @Test
    public void 없는_상품을_조회하면_예외가_발생한다() {
        ProductEntity result = jpaProductRepository.findById(100L).orElse(null);
        assertNull(result);
    }

    @Test
    public void 저장소로_조회한_엔티티로_변경_감지가_적용된다() {
        ProductEntity product = new ProductEntity(100L, "keyboard", 1000);
        jpaProductRepository.save(product);
        jpaProductRepository.flush();
        entityManager.clear();

        ProductEntity find_product = jpaProductRepository.findById(100L).orElse(null);
        assertNotNull(find_product);

        jpaProductPriceService.changePrice(100L, 4000);
        jpaProductRepository.flush();
        entityManager.clear();

        ProductEntity result = jpaProductRepository.findById(100L).orElse(null);
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("keyboard", result.getName());
        assertEquals(4000, result.getPrice());
    }
}
