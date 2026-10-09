package java.api.java.collection.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class MemoryRepository<E,ID> implements CrudRepository<E, ID> {
    private final Map<ID,E> storage = new HashMap<>();

    @Override
    public void save(ID id, E entity) {
        if(entity == null || id == null){
            throw new IllegalArgumentException("entity와 id에는 null 값을 저장할 수 없습니다.");
        }
        storage.put(id, entity);
    }

    @Override
    public E findById(ID id) {
        if(!storage.containsKey(id)) {
            throw new NoSuchElementException("데이터를 찾지 못했습니다.");
        }
        return storage.get(id);
    }

    @Override
    public List<E> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public boolean existsById(ID id) {
        return storage.containsKey(id);
    }

    @Override
    public void deleteById(ID id) {
        if(!storage.containsKey(id)) {
            throw new NoSuchElementException("삭제할 데이터를 찾지 못했습니다.");
        }
        storage.remove(id);
    }
}
