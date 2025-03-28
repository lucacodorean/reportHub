package com.reporthub.service;

import exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IEntityService<T> {
    T save(T entity);
    T findById(Long id)         throws NotFoundException;
    T findByKey(String key)     throws NotFoundException;
    List<T> findAll();
    boolean delete(T entity)    throws NotFoundException;
}
