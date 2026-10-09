package com.api.java.collection.sort;

import com.api.java.collection.sort.Product;
import com.api.java.collection.sort.ProductSortService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ProductSortServiceTest {

    @ParameterizedTest
    @MethodSource("priceSourceDataAsc")
    public void 상품가격을_오름차순으로_정렬한다(List<Product> products, List<Long> expectedId) {
        ProductSortService productSortService = new ProductSortService();

        List<Product> sortedList = productSortService.sortByPriceAscending(products);

        List<Long> sortedIdList = sortedList.stream()
                .map(Product::getId)
                .toList();

        assertEquals(expectedId, sortedIdList);
    }


    @ParameterizedTest
    @MethodSource("priceSourceDataDesc")
    public void 상품가격을_내림차순으로_정렬한다(List<Product> products, List<Long> expectedId) {
        ProductSortService productSortService = new ProductSortService();

        List<Product> sortedList = productSortService.sortByPriceDescending(products);

        List<Long> sortedIdList = sortedList.stream()
                .map(Product::getId)
                .toList();

        assertEquals(expectedId, sortedIdList);
    }

    @ParameterizedTest
    @MethodSource("stockPriceIdSourceData")
    public void 재고_내림차순_가격_오름차순_ID_오름차순으로_정렬한다(List<Product> products, List<Long> expectedId) {
        ProductSortService productSortService = new ProductSortService();

        List<Product> sortedList = productSortService.sortByStockDescendingAndPriceAscending(products);

        List<Long> sortedIdList = sortedList.stream()
                .map(Product::getId)
                .toList();

        assertEquals(expectedId, sortedIdList);
    }

    @Test
    public void 정렬_이후에도_원본_리스트는_변경되지_않는다() {
        ProductSortService productSortService = new ProductSortService();

        Product product1 = new Product(1L, "product1", 2000, 1);
        Product product2 = new Product(2L, "product2", 1000, 2);
        Product product3 = new Product(3L, "product3", 3000, 3);

        List<Product> products = new ArrayList<>(List.of(product1, product2, product3));

        productSortService.sortByPriceAscending(products);

        List<Long> originalIds = products.stream()
                .map(Product::getId)
                .toList();

        assertEquals(List.of(1L, 2L, 3L), originalIds);
    }

    @Test
    public void 반환된_리스트_수정시_예외를_반환한다() {
        ProductSortService productSortService = new ProductSortService();

        Product product1 = new Product(1L, "product1", 1000, 1);
        Product product2 = new Product(2L, "product2", 1000, 2);
        Product product3 = new Product(3L, "product3", 1000, 3);

        List<Product> products = List.of(product1, product2, product3);

        List<Product> sortedList = productSortService.sortByPriceAscending(products);

        assertThrows(UnsupportedOperationException.class, () ->
                sortedList.set(0, new Product(4L, "product4", 100, 4)));
    }

    @Test
    public void 빈_리스트를_전달하면_빈_리스트를_반환한다() {
        ProductSortService productSortService = new ProductSortService();

        List<Product> products = new ArrayList<>();

        List<Product> result = productSortService.sortByPriceAscending(products);

        assertTrue(result.isEmpty());
    }

    @Test
    public void 상품이_하나여도_정상적으로_반환한다() {
        ProductSortService productSortService = new ProductSortService();

        Product product1 = new Product(1L, "product1", 1000, 1);

        List<Product> products = new ArrayList<>(List.of(product1));

        List<Product> result = productSortService.sortByPriceAscending(products);

        assertEquals("product1", result.getFirst().getName());
    }

    @Test
    public void 상품리스트를_기본순서로_정렬한다() {
        ProductSortService productSortService = new ProductSortService();
        Product product1 = new Product(2L, "product1", 1000, 1);
        Product product2 = new Product(1L, "product2", 1000, 2);
        Product product3 = new Product(3L, "product3", 1000, 3);

        List<Product> products = new ArrayList<>(List.of(product1, product2, product3));

        List<Product> result = productSortService.sortByNaturalOrder(products);

        assertEquals(List.of(product2, product1, product3), result);
    }

    @Test
    public void 기본순서로_정렬된_리스트는_수정할_수_없다() {
        ProductSortService productSortService = new ProductSortService();
        Product product1 = new Product(2L, "product1", 1000, 1);
        Product product2 = new Product(1L, "product2", 1000, 2);
        Product product3 = new Product(3L, "product3", 1000, 3);

        List<Product> products = new ArrayList<>(List.of(product1, product2, product3));

        List<Product> result = productSortService.sortByNaturalOrder(products);

        assertThrows(UnsupportedOperationException.class, () ->
                result.add(new Product(4L, "product4", 100, 4))
        );
    }

    @Test
    public void 기본순서로_정렬후_원본_리스트는_변하지_않는다() {
        ProductSortService productSortService = new ProductSortService();
        Product product1 = new Product(2L, "product1", 1000, 1);
        Product product2 = new Product(1L, "product2", 1000, 2);
        Product product3 = new Product(3L, "product3", 1000, 3);

        List<Product> products = new ArrayList<>(List.of(product1, product2, product3));
        productSortService.sortByNaturalOrder(products);

        assertEquals(List.of(product1, product2, product3), products);
    }

    @Test
    public void 상품ID가_중복되는_리스트의_정렬을_검사한다() {
        ProductSortService productSortService = new ProductSortService();
        Product product1 = new Product(2L, "product1", 1000, 1);
        Product product2 = new Product(2L, "product2", 1000, 2);
        Product product3 = new Product(1L, "product3", 1000, 3);

        List<Product> products = new ArrayList<>(List.of(product1, product2, product3));
        List<Product> result = productSortService.sortByNaturalOrder(products);

        assertEquals(List.of(product3, product1, product2), result);

    }

    @Test
    public void 빈_리스트를_기본정렬하면_빈_리스트를_반환한다() {
        ProductSortService productSortService = new ProductSortService();

        List<Product> products = new ArrayList<>();
        List<Product> result = productSortService.sortByNaturalOrder(products);
        assertTrue(result.isEmpty());
    }

    @Test
    public void 상품이_한개여도_기본정렬을_수행한다() {
        ProductSortService productSortService = new ProductSortService();

        Product product1 = new Product(1L, "product1", 1000, 1);
        List<Product> products = new ArrayList<>(List.of(product1));
        List<Product> result = productSortService.sortByNaturalOrder(products);

        assertEquals(List.of(product1), result);
    }

    static Stream<Arguments> priceSourceDataAsc() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                product(1L, 1000, 1),
                                product(2L, 2000, 2),
                                product(3L, 3000, 3)
                        ),
                        List.of(1L, 2L, 3L)
                ),
                Arguments.of(
                        List.of(
                                product(1L, 2000, 1),
                                product(2L, 1000, 2),
                                product(3L, 3000, 3)
                        ),
                        List.of(2L, 1L, 3L)
                ));
    }

    static Stream<Arguments> priceSourceDataDesc() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                product(1L, 1000, 1),
                                product(2L, 2000, 2),
                                product(3L, 3000, 3)
                        ),
                        List.of(3L, 2L, 1L)
                ),
                Arguments.of(
                        List.of(
                                product(1L, 2000, 1),
                                product(2L, 1000, 2),
                                product(3L, 3000, 3)
                        ),
                        List.of(3L, 1L, 2L)
                )
        );
    }

    static Stream<Arguments> stockPriceIdSourceData() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                product(1L, 1000, 1),
                                product(2L, 2000, 2),
                                product(3L, 3000, 3)
                        ),
                        List.of(3L, 2L, 1L)
                ),
                Arguments.of(
                        List.of(
                                product(1L, 3000, 10),
                                product(2L, 1000, 20),
                                product(3L, 2000, 20),
                                product(4L, 1000, 20)
                        ),
                        List.of(2L, 4L, 3L, 1L)
                )
        );
    }

    private static Product product(Long id, int price, int stock) {
        return new Product(
                id,
                "product" + id,
                price,
                stock
        );
    }

}
