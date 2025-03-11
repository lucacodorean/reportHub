package com.reporthub.service.implementation;

import com.reporthub.entity.Tag;
import com.reporthub.repository.ITagRepository;
import com.reporthub.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ITagServiceImpl implements ITagService {

    @Autowired
    private ITagRepository tagRepository;

    public Tag save(Tag entity) { return tagRepository.save(entity); }

    public Tag findById(Long id) { return tagRepository.findById(id).orElse(null); }

    public Tag findByKey(String key) { return tagRepository.findByKey(key).orElse(null); }

    public List<Tag> findAll() { return tagRepository.findAll(); }

    public boolean delete(Tag entity) {
        if(!tagRepository.existsById(entity.getId())) return false;

        tagRepository.delete(entity);
        return true;
    }
}
