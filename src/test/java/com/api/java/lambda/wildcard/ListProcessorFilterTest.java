package com.api.java.lambda.wildcard;

import com.api.java.lambda.wildcard.ListProcessor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class ListProcessorFilterTest {
    private final ListProcessor processor = new ListProcessor();

    @Test
    public void Integer타입으로_filter를_호출한다() {
        List<Integer> source = List.of(1, 2, 3);

        List<Integer> result = processor.filter(
                source,
                data -> data % 2 == 0
        );

        assertEquals(List.of(2), result);
    }

    @Test
    public void Number타입으로_filter를_호출한다() {
        List<Number> source = List.of(-2, 1, 3);

        List<Number> result = processor.filter(
                source,
                data -> data.intValue() >= 0
        );

        assertEquals(List.of(1, 3), result);
    }

    @Test
    public void Integer와_Number타입으로_filter를_호출한다() {
        List<Integer> source = List.of(-2, 1, 3);
        Predicate<Number> condition = data -> data.doubleValue() >= 0;

        List<Integer> int_result = processor.filter(
                source,
                condition
        );

        List<Number> number_result = processor.filter(
                source,
                condition
        );

        assertEquals(List.of(1, 3), int_result);
        assertEquals(List.of(1, 3), number_result);
    }

    @Test
    public void 필터링된_리스트는_순서를_유지한다() {
        List<Integer> source = List.of(3, -1, 2, 3);

        List<Integer> result = processor.filter(
                source,
                data -> data >= 0
        );

        assertEquals(List.of(3, 2, 3), result);
    }

    @Test
    public void 필터링_이후_원본_리스트는_보존되어야한다() {
        List<Integer> source = new ArrayList<>(List.of(3, -1, 2, 3));

        processor.filter(
                source,
                data -> data >= 0
        );

        assertEquals(List.of(3, -1, 2, 3), source);
    }

    @Test
    public void 필터링된_리스트를_수정하면_예외를_반환한다() {
        List<Integer> source = List.of(3, -1, 2, 3);

        List<Integer> result = processor.filter(
                source,
                data -> data >= 0
        );

        assertThrows(UnsupportedOperationException.class, () ->
                result.add(4));
    }

    @Test
    public void 빈_리스트는_필터링_후_빈_리스트를_반환한다() {
        List<Integer> source = List.of();

        List<Integer> result = processor.filter(
                source,
                data -> data >= 0
        );

        assertTrue(result.isEmpty());
    }

    @Test
    public void 일치하는_요소가_없다면_빈_리스트를_반환한다() {
        List<Integer> source = List.of(3, -1, 2, 3);

        List<Integer> result = processor.filter(
                source,
                data -> data > 5
        );

        assertTrue(result.isEmpty());
    }

    @Test
    public void source가_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                processor.filter(
                        null,
                        data -> true
                ));
    }

    @Test
    public void condition이_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                processor.filter(
                        List.of(1, 2, 3),
                        null
                ));
    }

}
