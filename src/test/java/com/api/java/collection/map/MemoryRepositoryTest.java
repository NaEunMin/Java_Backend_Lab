package com.api.java.collection.map;

import com.api.java.collection.map.CrudRepository;
import com.api.java.collection.map.MemoryRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class MemoryRepositoryTest {
    private final CrudRepository<String, Long> repository = new MemoryRepository<>();

    @Test
    public void 서로_다른_ID로_값을_저장하고_조회할_수_있다(){
        repository.save(1L, "data1");
        repository.save(2L, "data2");

        assertEquals("data1", repository.findById(1L));
        assertEquals("data2", repository.findById(2L));
    }

    @Test
    public void null값을_저장하면_예외를_반환한다(){
        assertThrows(IllegalArgumentException.class, () -> {
            repository.save(1L, null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            repository.save(null, "data1");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            repository.save(null, null);
        });
    }

    @Test
    public void 같은_ID로_값을_저장하면_새로운_값이_저장된다(){
        repository.save(1L, "data1");
        repository.save(1L, "data2");

        assertEquals("data2", repository.findById(1L), "같은 ID로 값을 저장하면 새로운 값이 저장되어야 합니다");
    }

    @Test
    public void 존재하지_않는_ID를_조회하면_예외를_반환한다(){
        repository.save(1L, "data1");

        assertThrows(NoSuchElementException.class, ()->
                repository.findById(2L));
    }

    @Test
    public void 저장된_ID는_true_를_반환한다(){
        repository.save(1L, "data1");

        assertTrue(repository.existsById(1L));
    }

    @Test
    public void 데이터를_삭제하면_더이상_조회할_수_없다(){
        repository.save(1L, "data1");
        repository.deleteById(1L);

        assertFalse(repository.existsById(1L));

        assertThrows(NoSuchElementException.class, ()->
                repository.findById(1L));
    }

    @Test
    public void 존재하지_않는_ID를_삭제시도하면_예외를_반환한다(){
        repository.save(1L, "data1");

        assertThrows(NoSuchElementException.class, ()->
                repository.deleteById(2L));
    }

    @Test
    public void findAll은_모든_데이터를_반환한다(){
        repository.save(1L, "data1");
        repository.save(2L, "data2");

        List<String> values = repository.findAll();

        assertEquals(2, values.size());
        assertTrue(values.containsAll(List.of("data1", "data2")));
    }

    @Test
    public void findAll로_조회한_리스트는_외부에서_수정할_수_없다(){
        repository.save(1L, "data1");
        repository.save(2L, "data2");

        List<String> values = repository.findAll();

        assertThrows(UnsupportedOperationException.class, ()->
                values.set(0, "data3"), "불변 리스트는 수정할 수 없습니다.");

        assertEquals("data1", repository.findById(1L));
        assertEquals("data2", repository.findById(2L));
    }

}
