package com.api.java.collection.list;

import com.api.java.collection.list.ListChunk;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListChunkTest {
    private final ListChunk listChunk = new ListChunk();

    @Test
    public void source를_chunkSize대로_리스트로_분리한다() {
        List<String> source = new ArrayList<>(List.of("a", "b", "c", "d", "e"));

        List<List<String>> result = listChunk.chunk(source, 2);

        assertEquals(
                List.of(
                        List.of("a", "b"),
                        List.of("c", "d"),
                        List.of("e")
                ),
                result
        );
    }

    @Test
    public void 중첩되는_source를_chunkSize대로_리스트를_분리한다() {
        List<String> source = new ArrayList<>(List.of("a", "b", "a"));
        List<List<String>> result = listChunk.chunk(source, 2);

        assertEquals(
                List.of(
                        List.of("a", "b"),
                        List.of("a")
                ), result
        );
    }

    @Test
    public void null이_포함되는_source를_chunkSize대로_리스트를_분리한다() {
        List<String> source = Arrays.asList(null, "a", null);
        List<List<String>> result = listChunk.chunk(source, 2);

        assertEquals(
                Arrays.asList(
                        Arrays.asList(null, "a"),
                        Arrays.asList((String) null)
                ), result
        );

    }

    @Test
    public void source가_비어있다면_chunk메서드는_빈_결과를_반환한다() {
        List<List<String>> result = listChunk.chunk(List.of(), 2);
        assertTrue(result.isEmpty());
    }

    @Test
    public void chunk메서드는_원본리스트를_변경하지_않는다() {
        List<String> source = new ArrayList<>(List.of("a", "b", "c", "d", "e"));

        listChunk.chunk(source, 2);

        assertEquals(List.of("a", "b", "c", "d", "e"), source);
    }

    @Test
    public void source가_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                listChunk.chunk(null, 2));
    }

    @Test
    public void chunkSize가_0이하라면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                listChunk.chunk(List.of(), -1));
        assertThrows(IllegalArgumentException.class, () ->
                listChunk.chunk(List.of(), 0));
    }

    @Test
    public void 원본_리스트를_수정해도_결과_리스트는_변하지_않는다() {
        List<String> source = new ArrayList<>(List.of("a", "b", "c", "d", "e"));
        List<List<String>> result = listChunk.chunk(source, 2);
        source.add("f");

        assertEquals(
                List.of(
                        List.of("a", "b"),
                        List.of("c", "d"),
                        List.of("e")
                ),
                result
        );
    }

    @Test
    public void 결과_리스트를_수정해도_원본_리스트는_변하지_않는다() {
        List<String> source = new ArrayList<>(List.of("a", "b", "c", "d", "e"));
        List<List<String>> result = listChunk.chunk(source, 2);
        result.add(List.of("f"));
        result.get(0).add("g");

        assertEquals(List.of("a", "b", "c", "d", "e"), source);
    }

    @Test
    public void source가_chunkSize로_나누어_지는지_확인한다() {
        List<String> source = new ArrayList<>(List.of("a", "b", "c", "d", "e"));
        List<List<String>> result = listChunk.chunk(source, 1);

        assertEquals(
                List.of(
                        List.of("a"),
                        List.of("b"),
                        List.of("c"),
                        List.of("d"),
                        List.of("e")
                ), result
        );
    }
}
