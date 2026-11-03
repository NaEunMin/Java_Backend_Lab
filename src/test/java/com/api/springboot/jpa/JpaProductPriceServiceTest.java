package com.api.springboot.jpa;

import com.api.springboot.BackendStudyApplication;
import com.api.springboot.entity.ProductEntity;
import com.api.springboot.service.JpaProductPriceService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BackendStudyApplication.class)
@ActiveProfiles("h2")
public class JpaProductPriceServiceTest {

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into products (id, name, price) values (?, ?, ?)",
                300L, "keyboard", 3000);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("delete from products");
    }

    @Autowired
    private JpaProductPriceService service;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void 상품의_가격을_변경한다() {
        service.changePrice(300L, 4000);
        ProductEntity product = entityManager.find(ProductEntity.class, 300L);
        assertNotNull(product);
        assertEquals(4000, product.getPrice());
    }

    @Test
    public void flush_이후_예외발생후_롤백된다() {
        assertThrows(IllegalStateException.class, () -> service.changePriceThenFlushAndFail(300L, 4000));
        ProductEntity product = entityManager.find(ProductEntity.class, 300L);
        assertNotNull(product);
        assertEquals(3000, product.getPrice());
    }

    @Test
    public void 잘못된_가격이_들어오면_예외발생후_기존가격이_유지된다() {
        assertThrows(IllegalArgumentException.class,
                ()-> service.changePrice(300L, 0));

        ProductEntity product = entityManager.find(ProductEntity.class, 300L);
        assertNotNull(product);
        assertEquals(3000, product.getPrice());
    }
}
