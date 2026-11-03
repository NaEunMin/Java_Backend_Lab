package com.api.springboot.jpa;

import com.api.springboot.BackendStudyApplication;
import com.api.springboot.entity.ProductEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BackendStudyApplication.class)
@ActiveProfiles("h2")
@Transactional
public class DirtyCheckingTest {
    @Autowired
    private EntityManager entityManager;

    @Test
    public void 관리중인_엔티티는_변경_내용이_반영된다() {
        ProductEntity product = new ProductEntity(300L, "keyboard", 3000);
        entityManager.persist(product);
        assertTrue(entityManager.contains(product));

        entityManager.flush();
        entityManager.clear();

        ProductEntity find_product = entityManager.find(ProductEntity.class, 300L);
        assertNotNull(find_product);

        /*
            find()를 호출하여 영속성 컨텍스트에 엔티티가 등록이 된 후 필드 변경이 이루어짐을 유의
         */
        find_product.changePrice(4000);
        entityManager.flush();
        entityManager.clear();

        ProductEntity changed_product = entityManager.find(ProductEntity.class, 300L);
        assertNotNull(changed_product);
        assertEquals(300L, changed_product.getId());
        assertEquals("keyboard", changed_product.getName());
        assertEquals(4000, changed_product.getPrice());
    }

    @Test
    public void 관리에서_분리된_엔티티는_변경_감지가_적용되지_않는다() {
        ProductEntity product = new ProductEntity(300L, "keyboard", 3000);
        entityManager.persist(product);
        assertTrue(entityManager.contains(product));

        entityManager.flush();
        entityManager.clear();

        product.changePrice(4000);
        assertEquals(4000, product.getPrice());

        entityManager.flush();
        ProductEntity find_product = entityManager.find(ProductEntity.class, 300L);

        assertNotNull(find_product);
        assertEquals(300L, find_product.getId());
        assertEquals("keyboard", find_product.getName());
        assertEquals(3000, find_product.getPrice());

        assertNotSame(product, find_product);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void 잘못된_가격은_객체에_남지않는다(int param) {
        ProductEntity product = new ProductEntity(300L, "keyboard", 3000);
        entityManager.persist(product);
        assertTrue(entityManager.contains(product));
        entityManager.flush();

        assertThrows(IllegalArgumentException.class, () -> {
            product.changePrice(param);
        });

        entityManager.flush();
        entityManager.clear();

        ProductEntity find_product = entityManager.find(ProductEntity.class, 300L);
        assertNotNull(find_product);
        assertEquals(3000, find_product.getPrice());
    }
}
