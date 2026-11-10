package com.api.springboot.jpa;

import com.api.springboot.BackendStudyApplication;
import com.api.springboot.entity.CategoryEntity;
import com.api.springboot.entity.ProductEntity;
import com.api.springboot.repository.JpaProductRepository;
import com.api.springboot.service.JpaProductPriceService;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("""
                insert into products (id, name, price)
                values (50, 'speaker',  4000),
                       (40, 'mouse',    2000),
                       (20, 'keyboard', 2000),
                       (10, 'cable',    1999),
                       (60, 'adapter',  4001)
                """);
    }

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

    @Test
    public void persist는_전달한_객체_자체를_관리한다() {
        ProductEntity original = new ProductEntity(100L, "keyboard", 1000);
        entityManager.persist(original);
        assertTrue(entityManager.contains(original));

        original.changePrice(4000);
        entityManager.flush();
        entityManager.clear();
        ProductEntity result = jpaProductRepository.findById(100L).orElse(null);

        assertNotNull(result);
        assertEquals(4000, result.getPrice());
    }

    @Test
    public void 수동_ID_상품을_save하면_반환된_객체의_변경만_반영된다() {
        ProductEntity original = new ProductEntity(100L, "keyboard", 3000);
        ProductEntity saved = jpaProductRepository.save(original);

        assertNotSame(original, saved);
        assertFalse(entityManager.contains(original));
        assertTrue(entityManager.contains(saved));

        original.changePrice(9000);
        assertEquals(3000, saved.getPrice());

        saved.changePrice(4000);
        entityManager.flush();
        entityManager.clear();
        ProductEntity result = jpaProductRepository.findById(100L).orElse(null);
        assertNotNull(result);
        assertEquals(4000, result.getPrice());
    }

    @Test
    public void 가격_범위에_포함된_상품을_가격순으로_조회한다() {
        List<ProductEntity> queryMethodProducts = jpaProductRepository.findByPriceBetweenOrderByPriceAscIdAsc(2000, 4000);
        List<ProductEntity> jpqlProducts = jpaProductRepository.findInPriceRange(2000, 4000);

        List<Long> queryMethodProductsIds = queryMethodProducts.stream()
                .map(ProductEntity::getId)
                .toList();

        List<Long> jpqlProductsIds = jpqlProducts.stream()
                .map(ProductEntity::getId)
                .toList();

        assertEquals(List.of(20L, 40L, 50L), queryMethodProductsIds);
        assertEquals(List.of(20L, 40L, 50L), jpqlProductsIds);
    }

    @Test
    public void 가격_범위에_해당하는_상품이_없으면_빈_리스트를_반환한다() {
        List<ProductEntity> queryMethodProducts = jpaProductRepository.findByPriceBetweenOrderByPriceAscIdAsc(5000, 6000);
        List<ProductEntity> jpqlProducts = jpaProductRepository.findInPriceRange(5000, 6000);

        assertTrue(queryMethodProducts.isEmpty());
        assertTrue(jpqlProducts.isEmpty());
    }

    @Test
    public void 같은_가격의_상품도_ID순으로_페이지_경계를_넘는다() {
        insertPagingProducts();
        Sort sort = Sort.by(Sort.Order.asc("price"), Sort.Order.asc("id"));
        Pageable pageable1 = PageRequest.of(0, 2, sort);

        Page<ProductEntity> pagingProducts = jpaProductRepository.findByPriceBetween(2000, 4001, pageable1);
        List<Long> pagingProductsIds1 = pagingProducts.map(ProductEntity::getId).getContent();
        assertTrue(pagingProducts.hasNext());
        assertEquals(List.of(20L, 40L), pagingProductsIds1);
        assertEquals(5L, pagingProducts.getTotalElements());

        Pageable pageable2 = PageRequest.of(1, 2, sort);

        Page<ProductEntity> pagingProducts2 = jpaProductRepository.findByPriceBetween(2000, 4001, pageable2);
        List<Long> pagingProductsIds2 = pagingProducts2.map(ProductEntity::getId).getContent();
        assertTrue(pagingProducts2.hasNext());
        assertEquals(List.of(55L, 50L), pagingProductsIds2);
    }

    @Test
    public void 마지막_페이지는_한_건이지만_전체_검색_결과는_다섯_건이다() {
        insertPagingProducts();
        Sort sort = Sort.by(Sort.Order.asc("price"), Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(2, 2, sort);

        Page<ProductEntity> pagingProducts = jpaProductRepository.findByPriceBetween(2000, 4001, pageable);
        assertFalse(pagingProducts.hasNext());
        List<Long> pagingProductsIds = pagingProducts.map(ProductEntity::getId).getContent();

        assertEquals(List.of(60L), pagingProductsIds);
        assertEquals(1, pagingProducts.getNumberOfElements());
        assertEquals(5, pagingProducts.getTotalElements());
        assertEquals(3, pagingProducts.getTotalPages());
    }

    @Test
    public void 카테고리_이름을_처음_읽을때_지연로딩된다() {
        CategoryEntity category = new CategoryEntity(1L, "주변기기");
        entityManager.persist(category);

        ProductEntity product = new ProductEntity(1L, "keyboard", 1000, category);
        entityManager.persist(product);
        entityManager.flush();
        entityManager.clear();

        ProductEntity findProduct = jpaProductRepository.findById(1L).orElse(null);
        CategoryEntity loadedCategory = findProduct.getCategory();
        assertNotNull(loadedCategory);
        assertFalse(Hibernate.isInitialized(loadedCategory));

        String categoryName = loadedCategory.getName(); // SELECT log 확인 -> SELECT가 출력됨
        assertTrue(Hibernate.isInitialized(loadedCategory));

        categoryName = loadedCategory.getName(); // SELECT log 확인 -> SELECT가 출력되지 않음
    }

    @Test
    public void 카테고리의_상품_목록은_처음_조회할때_로딩된다() {
        CategoryEntity peripherals = new CategoryEntity(1L, "주변기기");
        CategoryEntity books = new CategoryEntity(2L, "도서");
        entityManager.persist(peripherals);
        entityManager.persist(books);

        ProductEntity keyboard = new ProductEntity(101L, "키보드", 3000, peripherals);
        ProductEntity mouse = new ProductEntity(102L, "마우스", 2000, peripherals);
        ProductEntity javaBook = new ProductEntity(103L, "자바책", 5000, books);

        peripherals.getProducts().add(keyboard);
        peripherals.getProducts().add(mouse);
        books.getProducts().add(javaBook);

        entityManager.persist(keyboard);
        entityManager.persist(mouse);
        entityManager.persist(javaBook);

        entityManager.flush();
        entityManager.clear();

        CategoryEntity category = entityManager.find(CategoryEntity.class, 1L); // SELECT 쿼리 log 확인
        List<ProductEntity> products = category.getProducts();
        assertFalse(Hibernate.isInitialized(products));

        // SELECT 쿼리 log 확인
        List<Long> productIds = products.stream()
                .map(ProductEntity::getId)
                .toList();

        assertTrue(Hibernate.isInitialized(products));
        assertEquals(List.of(101L, 102L), productIds);

        productIds = products.stream()
                .map(ProductEntity::getId)
                .toList(); // 재조회 시 SELECT 쿼리 log 안나옴
    }

    @Test
    public void 카테고리별_상품_순회에서_N플러스1이_발생한다() {
        saveCategoryAndProduct();

        List<CategoryEntity> categories = entityManager.createQuery(
                "select c from CategoryEntity c order by c.id",
                CategoryEntity.class).getResultList();

        assertEquals(3, categories.size());
        assertEquals(List.of("주변기기", "도서", "식품"), categories.stream().map(CategoryEntity::getName).toList());

        List<Long> peripheralsProductIds = categories.getFirst().getProducts().stream()
                .map(ProductEntity::getId)
                .sorted()
                .toList();

        assertEquals(List.of(101L, 102L), peripheralsProductIds);
        assertTrue(Hibernate.isInitialized(categories.getFirst().getProducts()));
        assertFalse(Hibernate.isInitialized(categories.get(1).getProducts()));
        assertFalse(Hibernate.isInitialized(categories.get(2).getProducts()));

        List<Long> booksProductIds = categories.get(1).getProducts().stream()
                .map(ProductEntity::getId)
                .sorted()
                .toList();

        assertEquals(List.of(103L), booksProductIds);
        assertTrue(Hibernate.isInitialized(categories.get(1).getProducts()));
        assertFalse(Hibernate.isInitialized(categories.get(2).getProducts()));

        List<Long> foodsProductIds = categories.get(2).getProducts().stream()
                .map(ProductEntity::getId)
                .sorted()
                .toList();

        assertEquals(List.of(104L), foodsProductIds);
        assertTrue(Hibernate.isInitialized(categories.get(2).getProducts()));

        // 재조회를 하여 추가 쿼리가 발생하는지 확인
        peripheralsProductIds = categories.getFirst().getProducts().stream()
                .map(ProductEntity::getId)
                .toList();

        booksProductIds = categories.get(1).getProducts().stream()
                .map(ProductEntity::getId)
                .toList();

        foodsProductIds = categories.get(2).getProducts().stream()
                .map(ProductEntity::getId)
                .toList();
    }

    private void insertPagingProducts() {
        jdbcTemplate.update("""
                insert into products (id, name, price)
                values (55, 'hub', 2000), (70, 'stand', 4002)
                """);
    }

    private void saveCategoryAndProduct() {
        CategoryEntity peripherals = new CategoryEntity(1L, "주변기기");
        CategoryEntity books = new CategoryEntity(2L, "도서");
        CategoryEntity foods = new CategoryEntity(3L, "식품");
        entityManager.persist(peripherals);
        entityManager.persist(books);
        entityManager.persist(foods);

        ProductEntity keyboard = new ProductEntity(101L, "keyboard", 1000, peripherals);
        ProductEntity mouse = new ProductEntity(102L, "mouse", 2000, peripherals);

        ProductEntity javaBook = new ProductEntity(103L, "java", 5000, books);

        ProductEntity potato = new ProductEntity(104L, "potato", 3000, foods);

        peripherals.getProducts().add(keyboard);
        peripherals.getProducts().add(mouse);

        books.getProducts().add(javaBook);

        foods.getProducts().add(potato);

        entityManager.persist(keyboard);
        entityManager.persist(mouse);

        entityManager.persist(javaBook);

        entityManager.persist(potato);

        entityManager.flush();
        entityManager.clear();
    }

}
