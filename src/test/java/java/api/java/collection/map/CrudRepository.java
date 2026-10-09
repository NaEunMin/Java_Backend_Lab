package java.api.java.collection.map;

import java.util.List;

public interface CrudRepository<E,ID> {
    void save(ID id, E entity);

    E findById(ID id);

    List<E> findAll();

    boolean existsById(ID id);

    void deleteById(ID id);
}
