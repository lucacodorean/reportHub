package com.reporthub.service.implementation;

import com.reporthub.dto.TagDTO;
import com.reporthub.entity.Tag;
import com.reporthub.repository.ITagRepository;
import com.reporthub.request.api.v1.IRequest;
import com.reporthub.request.api.v1.TagStoreRequest;
import com.reporthub.service.ITagService;
import com.reporthub.exception.NotFoundException;
import com.reporthub.service.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public Response<TagDTO> retrieveDTO(String key)  {
        Map<String, String> message = new HashMap<>();
        try {
            Tag tag = this.findByKey(key);
            if (tag == null) throw new NotFoundException("Tag not found");
            return new Response<>(new TagDTO(this.save(tag)), null);
        } catch (NotFoundException e) { message.put("message", "Tag not found"); }
        return new Response<>(null, message);
    }

    @Override
    public List<TagDTO> all() { return this.findAll().stream().map(TagDTO::new).collect(Collectors.toList()); }

    @Override
    public Response<TagDTO> create(TagStoreRequest request) {
        Tag tag = new Tag();
        tag.setName(request.getName());
        return new Response<>(new TagDTO(this.save(tag)), null);
    }

    @Override
    public Response<TagDTO> update(String key, IRequest request) { return null; }
}
