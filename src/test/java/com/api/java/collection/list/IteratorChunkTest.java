package com.api.java.collection.list;

import com.api.java.collection.iterator.Batch;
import com.api.java.collection.list.ListChunk;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IteratorChunkTest {
    private final ListChunk listChunk = new ListChunk();

    @Test
    public void source를_chunkSize로_분리한다() {
        Batch<String> source = new Batch<>(List.of("a", "b", "c"));

        List<List<String>> result = listChunk.iteratorChunk(source, 2);

        assertEquals(
                List.of(
                        List.of("a", "b"),
                        List.of("c")
                ),
                result
        );

    }

    @Test
    public void 중첩되는_source를_chunkSize로_분리한다() {
        Batch<String> source = new Batch<>(List.of("a", "b", "a"));
        List<List<String>> result = listChunk.iteratorChunk(source, 2);

        assertEquals(
                List.of(
                        List.of("a", "b"),
                        List.of("a")
                ),
                result
        );
    }

    @Test
    public void source가_비어있다면_빈_결과를_반환한다() {
        Batch<String> source = new Batch<>(List.of());

        List<List<String>> result = listChunk.iteratorChunk(source, 2);

        assertTrue(result.isEmpty());
    }

    @Test
    public void source가_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                listChunk.iteratorChunk(null, 2));
    }

    @Test
    public void chunkSize가_0이하라면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                listChunk.iteratorChunk(new Batch<>(List.of()), 0));

        assertThrows(IllegalArgumentException.class, () ->
                listChunk.iteratorChunk(new Batch<>(List.of()), -1));
    }

    @Test
    public void source가_chunkSize로_나누어_떨어지는지_확인한다() {
        Batch<String> source = new Batch<>(List.of("a", "b", "c"));
        List<List<String>> result = listChunk.iteratorChunk(source, 1);

        assertEquals(
                List.of(
                        List.of("a"),
                        List.of("b"),
                        List.of("c")
                ), result
        );
    }

    @Test
    void null_요소를_유지하며_분리한다() {
        Iterable<String> source = Arrays.asList(null, "a", null);

        List<List<String>> result = listChunk.iteratorChunk(source, 2);

        assertEquals(
                List.of(
                        Arrays.asList(null, "a"),
                        Arrays.asList((String) null)
                ),
                result
        );
    }
}
