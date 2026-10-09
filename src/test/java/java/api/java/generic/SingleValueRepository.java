package java.api.java.generic;

import java.util.NoSuchElementException;
import java.util.Objects;

public class SingleValueRepository <E, ID> implements Repository<E, ID> {
    private ID id;
    private E entity;
    private boolean saved;

    @Override
    public void save(ID id, E entity) {
       this.id = id;
       this.entity = entity;
       this.saved = true;
    }

    @Override
    public E findById(ID id) {
        if(!Objects.equals(this.id, id) || !saved) {
            throw new NoSuchElementException("데이터를 찾을 수 없습니다.");
        }
        return this.entity;
    }
}
