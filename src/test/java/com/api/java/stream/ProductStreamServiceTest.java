package com.api.java.stream;

import com.api.java.collection.sort.Product;
import com.api.java.stream.ProductStreamService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductStreamServiceTest {
    private final ProductStreamService productStreamService = new ProductStreamService();

    @Test
    public void 재고가_없는_상품은_제외한다() {
        Product product1 = new Product(1L, "product1", 1000, 1);
        Product product2 = new Product(2L, "product2", 1000, 2);
        Product product3 = new Product(3L, "product3", 1000, 0);

        List<String> result = productStreamService.findTopAvailableProductNames(List.of(product1, product2, product3), 3);

        assertEquals(2, result.size());
        assertEquals(List.of("product1", "product2"), result);
    }

    @Test
    public void 가격을_오름차순으로_정렬한다() {
        Product product1 = new Product(1L, "product1", 3000, 1);
        Product product2 = new Product(2L, "product2", 1000, 2);
        Product product3 = new Product(3L, "product3", 2000, 3);

        List<String> result = productStreamService.findTopAvailableProductNames(List.of(product1, product2, product3), 3);

        assertEquals(List.of("product2", "product3", "product1"), result);
    }

    @Test
    public void 가격이_같으면_ID를_오름차순으로_정렬한다() {
        List<Product> products = List.of(
                new Product(2L, "product2", 1000, 1),
                new Product(1L, "product1", 1000, 1)
        );

        List<String> result = productStreamService.findTopAvailableProductNames(products, 2);

        assertEquals(List.of("product1", "product2"), result);
    }

    @Test
    public void 상품을_limit만큼_반환한다() {
        Product product1 = new Product(1L, "product1", 1000, 1);
        Product product2 = new Product(2L, "product2", 2000, 2);
        Product product3 = new Product(3L, "product3", 3000, 3);
        Product product4 = new Product(4L, "product4", 4000, 4);

        List<String> result = productStreamService.findTopAvailableProductNames(List.of(product1, product2, product3, product4), 2);

        assertEquals(2, result.size());
        assertEquals(List.of("product1", "product2"), result);
    }

    @Test
    public void limit가_0이면_빈_리스트를_반환한다() {
        Product product1 = new Product(1L, "product1", 1000, 1);
        Product product2 = new Product(2L, "product2", 1000, 2);

        List<String> result = productStreamService.findTopAvailableProductNames(List.of(product1, product2), 0);

        assertTrue(result.isEmpty());
    }

    @Test
    public void 음수_limit는_예외를_반환한다() {
        Product product1 = new Product(1L, "product1", 1000, 1);

        assertThrows(IllegalArgumentException.class, () ->
                productStreamService.findTopAvailableProductNames(List.of(product1), -1));
    }

    @Test
    public void null입력은_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                productStreamService.findTopAvailableProductNames(null, 1));
    }

    @Test
    public void 반환리스트는_수정할_수_없다() {
        Product product1 = new Product(1L, "product1", 1000, 1);

        List<String> result = productStreamService.findTopAvailableProductNames(List.of(product1), 1);

        assertThrows(UnsupportedOperationException.class, () ->
                result.add("product2"));
    }

    @Test
    public void 원본리스트는_수정되지_않는다() {
        List<Product> products = new ArrayList<>(List.of(
                new Product(1L, "product1", 2000, 1),
                new Product(2L, "product2", 1000, 2)
        )
        );

        List<String> sortedList = productStreamService.findTopAvailableProductNames(products, 1);

        List<String> result = products.stream()
                .map(Product::getName)
                .toList();

        assertEquals(List.of("product1", "product2"), result);
    }

    @Test
    public void 전체_재고가치를_계산한다() {
        Product product1 = new Product(1L, "product1", 1000, 1);
        Product product2 = new Product(2L, "product2", 1000, 2);

        long result = productStreamService.calculateTotalInventoryValue(List.of(product1, product2));

        assertEquals(3000, result);
    }

    @Test
    public void 빈_상품목록의_재고가치는_0이다() {
        long result = productStreamService.calculateTotalInventoryValue(List.of());
        assertEquals(0, result);
    }

    @Test
    public void 재고가_0인_상품은_합계에_영향을_주지않는다() {
        Product product1 = new Product(1L, "product1", 1000, 1);
        Product product2 = new Product(2L, "product2", 1000, 0);

        long result = productStreamService.calculateTotalInventoryValue(List.of(product1, product2));

        assertEquals(1000, result);
    }

    @Test
    public void 재고리스트가_null일때_예외를_반환한다(){
        assertThrows(IllegalArgumentException.class, () ->
                productStreamService.calculateTotalInventoryValue(null));
    }
}
