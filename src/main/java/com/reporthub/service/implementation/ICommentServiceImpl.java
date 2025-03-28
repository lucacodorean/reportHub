package com.reporthub.service.implementation;

import com.reporthub.entity.Comment;
import com.reporthub.repository.ICommentRepository;
import com.reporthub.service.ICommentService;
import exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ICommentServiceImpl implements ICommentService {

    @Autowired
    private ICommentRepository commentRepository;

    public Comment save(Comment entity) {
        return commentRepository.save(entity);
    }

    public Comment findById(Long id) throws NotFoundException {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent()) return comment.get();
        else throw new NotFoundException("Comment not found");
    }

    public Comment findByKey(String key) throws NotFoundException {
        Optional<Comment> comment = commentRepository.findByKey(key);
        if (comment.isPresent()) return comment.get();
        else throw new NotFoundException("Comment not found");
    }

    public List<Comment> findAll() { return commentRepository.findAll(); }

    public boolean delete(Comment entity) throws NotFoundException {
        if(!commentRepository.existsById(entity.getId())) throw new NotFoundException("Comment not found");
        commentRepository.delete(entity);
        return true;
    }
}
