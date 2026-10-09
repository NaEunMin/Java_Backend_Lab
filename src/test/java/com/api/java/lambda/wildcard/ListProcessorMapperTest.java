package com.api.java.lambda.wildcard;

import com.api.java.lambda.wildcard.ListProcessor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class ListProcessorMapperTest {
    private final ListProcessor listProcessor = new ListProcessor();

    @Test
    public void 문자열에서_문자열크기를_받아온다() {
        List<String> source = List.of("Java", "Jpa", "Spring");

        List<Integer> result = listProcessor.map(
                source,
                String::length
        );

        assertEquals(List.of(4, 3, 6), result);
    }

    @Test
    public void Integer타입을_Number타입으로_받는다() {
        List<Integer> source = List.of(3, 2, 3);
        Function<Number, Integer> mapper = data -> data.intValue() * 2;

        List<Number> numberInput = listProcessor.<Number, Number>map(
                source,
                mapper
        );

        List<Number> intInput = listProcessor.<Integer, Number>map(
                source,
                mapper
        );


        assertEquals(List.of(6, 4, 6), numberInput);
        assertEquals(List.of(6, 4, 6), intInput);
    }

    @Test
    public void map이후_원본리스트는_유지된다() {
        List<Integer> source = new ArrayList<>(List.of(1, 2, 3));
        Function<Number, Integer> mapper = data -> data.intValue() * 2;

        listProcessor.map(
                source,
                mapper
        );

        assertEquals(List.of(1, 2, 3), source);
    }

    @Test
    public void map결과리스트를_수정하면_예외를_반환한다() {
        List<Integer> source = List.of(1, 2, 3);
        Function<Number, Integer> mapper = data -> data.intValue() * 2;
        List<Integer> result = listProcessor.map(
                source,
                mapper
        );

        assertThrows(UnsupportedOperationException.class, () ->
                result.add(4));
    }

    @Test
    public void source가_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                listProcessor.map(
                        null,
                        data -> true
                ));
    }

    @Test
    public void mapper가_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                listProcessor.map(
                        List.of(1, 2, 3),
                        null
                ));
    }

    @Test
    public void 반환결과가_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                listProcessor.map(
                        List.of(1, 2, 3),
                        data -> null
                ));
    }

    @Test
    void 빈_입력을_변환하면_빈_리스트를_반환한다() {
        List<Integer> source = List.of();
        Function<Number, Integer> mapper = number -> number.intValue() * 2;

        List<Number> result = listProcessor.map(source, mapper);

        assertTrue(result.isEmpty());
    }

}
