package uz.pdp.daos;

import org.springframework.stereotype.Component;

import java.util.List;

@Component

public interface BaseDao<T> {
    void save(T entity);
    void update(T entity);
    void delete(int entity);
    T getById(int id);
    List<T> getAll();
}
