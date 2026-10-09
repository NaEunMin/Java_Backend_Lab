package java.lambda;

import java.api.java.collection.sort.Product;
import java.api.java.lambda.ListProcessor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListProcessorFilterTest {

    private final ListProcessor listProcessor = new ListProcessor();

    @Test
    public void 짝수만_필터링하여_제공한다() {
        List<Integer> result = listProcessor.filter(
                List.of(1, 2, 3, 4),
                number -> number % 2 == 0
        );

        assertEquals(List.of(2, 4), result);
    }

    @Test
    public void 특정금액_이상인_상품만_필터링한다() {
        Product product1 = new Product(1L, "product1", 1000, 1);
        Product product2 = new Product(2L, "product2", 2000, 2);

        List<Product> result = listProcessor.filter(
                List.of(product1, product2),
                product -> product.getPrice() >= 2000
        );

        assertEquals(List.of(product2), result);
    }

    @Test
    public void 아무값도_필터링되지않으면_빈_결과를_반환한다() {
        List<Integer> result = listProcessor.filter(
                List.of(1, 1, 1, 1),
                number -> number % 2 == 0
        );

        assertTrue(result.isEmpty());
    }

    @Test
    public void 빈_입력은_빈_리스트를_반환한다() {
        List<Integer> result = listProcessor.filter(
                List.of(),
                number -> number % 2 == 0
        );

        assertTrue(result.isEmpty());
    }

    @Test
    public void 반환_리스트는_수정할_수_없다() {
        List<Integer> result = listProcessor.filter(
                List.of(1, 1, 1, 1),
                number -> number % 2 == 0
        );

        assertThrows(UnsupportedOperationException.class, () ->
                result.add(1));
    }

    @Test
    public void source와_condition이_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                listProcessor.filter(
                        null,
                        null
                )
        );
    }

    @Test
    public void source가_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                listProcessor.filter(
                        null,
                        value -> true
                ));
    }

    @Test
    public void condition이_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                listProcessor.filter(
                        List.of(1L, 2L, 3L),
                        null
                ));
    }

}
