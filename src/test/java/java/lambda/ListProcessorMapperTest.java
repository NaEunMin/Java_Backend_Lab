package java.lambda;

import java.api.java.collection.sort.Product;
import java.api.java.lambda.ListProcessor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListProcessorMapperTest {
    private final ListProcessor listProcessor = new ListProcessor();

    @Test
    public void 문자열을_문자열_길이로_반환한다() { //입력과 출력이 달라도 정상적으로 동작함을 보이는 테스트 코드
        List<Integer> result = listProcessor.map(
                List.of("java", "spring", "jpa"),
                String::length
        );

        assertEquals(List.of(4, 6, 3), result);
    }

    @Test
    public void 상품을_상품ID로_반환한다() {
        List<Long> result = listProcessor.map(
                List.of(
                        new Product(1L, "product1", 1000, 1),
                        new Product(2L, "product2", 2000, 2),
                        new Product(3L, "product3", 3000, 3)
                ),
                Product::getId
        );

        assertEquals(List.of(1L, 2L, 3L), result);
    }

    @Test
    public void mapper가_null을_반환하면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () -> {
            listProcessor.map(
                    List.of("java", "spring", "jpa"),
                    null
            );
        });
    }

    @Test
    public void source가_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () -> {
            listProcessor.map(
                    null,
                    value -> true
            );
        });
    }

    @Test
    public void mapper가_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () -> {
            listProcessor.map(
                    List.of(1L, 2L, 3L),
                    null
            );
        });
    }

    @Test
    public void mapper결과가_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () -> {
            listProcessor.map(
                    List.of("java", "spring", "jpa"),
                    value -> null
            );
        });
    }

    @Test
    public void 반환된_리스트는_수정할_수_없다() {
        List<Integer> result = listProcessor.map(
                List.of("java", "spring", "jpa"),
                String::length
        );

        assertThrows(UnsupportedOperationException.class, () -> {
            result.add(4);
        });
    }

}
