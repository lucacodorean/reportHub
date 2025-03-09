package com.reporthub.service;

import com.reporthub.entity.Postable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IPostService extends IEntityService<Postable> {
    Postable save(Postable entity);
    Postable findById(Long id);
    Postable findByKey(String key);
    List<Postable> findAll();
    boolean delete(Postable post);
}
