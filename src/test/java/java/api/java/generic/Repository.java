package java.api.java.generic;

public interface Repository <E, ID>{

    void save(ID id, E entity);

    E findById(ID id);
}
