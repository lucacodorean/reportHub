package com.reporthub.service.implementation;

import com.reporthub.entity.Tag;
import com.reporthub.repository.ITagRepository;
import com.reporthub.service.ITagService;
import exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ITagServiceImpl implements ITagService {

    @Autowired
    private ITagRepository tagRepository;

    public Tag save(Tag entity) { return tagRepository.save(entity); }

    public Tag findById(Long id) throws NotFoundException  {
        Optional<Tag> tag = tagRepository.findById(id);
        if(tag.isPresent()) return tag.get();
        else throw new NotFoundException("Tag not found");
    }

    public Tag findByKey(String key) throws NotFoundException {
        Optional<Tag> tag = tagRepository.findByKey(key);
        if(tag.isPresent()) return tag.get();
        else throw new NotFoundException("Tag not found");
    }

    public List<Tag> findAll() { return tagRepository.findAll(); }

    public boolean delete(Tag entity) throws NotFoundException {
        if(!tagRepository.existsById(entity.getId())) return false;

        tagRepository.delete(entity);
        return true;
    }
}
