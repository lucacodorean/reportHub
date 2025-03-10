package com.reporthub.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IEntityService<T> {
    T save(T entity);
    T findById(Long id);
    T findByKey(String key);
    List<T> findAll();
    // T update(T entity); de implementat pentru editare de postari/commenturi/useri....
    boolean delete(T entity);
}
