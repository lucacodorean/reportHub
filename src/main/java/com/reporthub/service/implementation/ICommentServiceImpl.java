package com.reporthub.service.implementation;

import com.reporthub.entity.Comment;
import com.reporthub.repository.ICommentRepository;
import com.reporthub.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ICommentServiceImpl implements ICommentService {

    @Autowired private ICommentRepository commentRepository;

    public Comment save(Comment entity) {
        return commentRepository.save(entity);
    }

    public Comment findById(Long id) { return commentRepository.findById(id).orElse(null); }

    public Comment findByKey(String key) { return commentRepository.findByKey(key).orElse(null); }

    public List<Comment> findAll() { return commentRepository.findAll(); }

    public boolean delete(Comment entity) {
        if(!commentRepository.existsById(entity.getId())) return false;

        commentRepository.delete(entity);
        return true;
    }
}
