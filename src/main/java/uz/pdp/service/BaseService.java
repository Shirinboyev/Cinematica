package uz.pdp.service;

import java.util.List;

public interface BaseService <T>{


    void save(T entity);
    void update(T entity);
    void delete(int entity);
    T getById(int id);
    List<T> getAll();

}
