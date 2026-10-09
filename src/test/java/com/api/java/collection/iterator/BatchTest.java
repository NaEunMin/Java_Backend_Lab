package com.api.java.collection.iterator;

import com.api.java.collection.iterator.Batch;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class BatchTest {

    @Test
    public void Iterator가_저장된_순서대로_반환한다(){
        Batch<String> batch = new Batch<>(List.of("a", "b", "c"));

        Iterator<String> iterator = batch.iterator();

        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
    }

    @Test
    public void 순회가_끝나면_hasNext는_false가_반환된다(){
        Batch<String> batch = new Batch<>(List.of("a"));

        Iterator<String> iterator = batch.iterator();

        iterator.next();

        assertFalse(iterator.hasNext());
    }

    @Test
    public void 순회가_끝나면_next는_예외를_발생시킨다(){
        Batch<String> batch = new Batch<>(List.of("a"));

        Iterator<String> iterator = batch.iterator();

        iterator.next();

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void 향상된_for문_을_사용할_수_있다(){
        Batch<String> batch = new Batch<>(List.of("a","b","c"));

        List<String> result = new ArrayList<>();

        for(String s : batch){
            result.add(s);
        }

        assertEquals(List.of("a","b","c"), result);
    }

    @Test
    public void 두_iterator는_독립적으로_동작한다(){
        Batch<String> batch = new Batch<>(List.of("a","b","c"));

        Iterator<String> iterator1 = batch.iterator();
        Iterator<String> iterator2 = batch.iterator();

        assertEquals("a", iterator1.next());
        assertEquals("b", iterator1.next());

        assertEquals("a", iterator2.next());
    }

    @Test
    public void 생성자에_전달한_리스트변경이_batch에_영향을_주지_않는다(){
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        Batch<String> batch = new Batch<>(list);
        Iterator<String> iterator = batch.iterator();

        list.add("d");

        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());

        assertEquals(3, batch.size());
        assertEquals(4, list.size());
    }

    @Test
    public void 빈_Batch의_iterator는_순환할_값이_없다(){
        Batch<String> batch = new Batch<>(List.of());

        Iterator<String> iterator = batch.iterator();

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }



}
