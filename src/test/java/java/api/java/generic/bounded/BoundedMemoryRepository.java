package java.api.java.generic.bounded;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class BoundedMemoryRepository<E extends Identifiable<ID>, ID> {

    private final Map<ID, E> storage = new HashMap<>();

    public void save(E entity){
        if(entity == null){
            throw new IllegalArgumentException("entity는 null일 수 없습니다.");
        }

        ID entityId = entity.getId();

        if(entityId == null){
            throw new IllegalArgumentException("entity의 Id는 null일 수 없습니다.");
        }

        //Purchase의 ID를 가져와서 entity의 ID로 등록
        storage.put(entityId, entity);
    }

    public E findById(ID id) {
        if(id == null){
            throw new IllegalArgumentException("id는 null일 수 없습니다.");
        }

        E entity = storage.get(id);
        if(entity == null){
            throw new NoSuchElementException("id에 해당하는 entity를 찾을 수 없습니다.");
        }
        return entity;
    }
}
