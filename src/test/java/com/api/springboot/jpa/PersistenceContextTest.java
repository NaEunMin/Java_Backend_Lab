package com.api.springboot.jpa;

import com.api.springboot.BackendStudyApplication;
import com.api.springboot.entity.ProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BackendStudyApplication.class)
@ActiveProfiles("h2")
@Transactional
public class PersistenceContextTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void 저장한_엔티티를_다시_조회한다() {
        ProductEntity product = new ProductEntity(200L, "keyboard", 2000);
        assertFalse(entityManager.contains(product));

        // 영속성 컨텍스트에 등록하고 등록이 되었는지 확인
        entityManager.persist(product);
        assertTrue(entityManager.contains(product));

        entityManager.flush();
        entityManager.clear();
        ProductEntity result = entityManager.find(ProductEntity.class, 200L);

        assertNotNull(result);
        assertEquals(200L, result.getId());
        assertEquals("keyboard", result.getName());
        assertEquals(2000, result.getPrice());
    }

    @Test
    public void 같은_식별자를_조회하면_같은_인스턴스를_반환한다() {
        ProductEntity product = new ProductEntity(200L, "keyboard", 2000);

        entityManager.persist(product);
        entityManager.flush();
        entityManager.clear();

        ProductEntity first = entityManager.find(ProductEntity.class, 200L);
        ProductEntity second = entityManager.find(ProductEntity.class, 200L);

        assertNotNull(first);
        assertNotNull(second);

        assertSame(first, second);

        assertEquals(200L, first.getId());
        assertEquals("keyboard", first.getName());
        assertEquals(2000, first.getPrice());

        assertEquals(200L, second.getId());
        assertEquals("keyboard", second.getName());
        assertEquals(2000, second.getPrice());
    }

    @Test
    public void clear후_객체_조회_시_다른_객체를_반환한다() {
        ProductEntity product = new ProductEntity(200L, "keyboard", 2000);

        entityManager.persist(product);
        entityManager.flush();
        entityManager.clear();

        ProductEntity result = entityManager.find(ProductEntity.class, 200L);
        assertNotNull(result);
        assertNotSame(product, result);

        assertEquals(200L, result.getId());
        assertEquals("keyboard", result.getName());
        assertEquals(2000, result.getPrice());
    }


}
