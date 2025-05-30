package com.reporthub.service.implementation;

import com.reporthub.dto.CommentDTO;
import com.reporthub.entity.Comment;
import com.reporthub.entity.auth.Authenticated;
import com.reporthub.repository.ICommentRepository;
import com.reporthub.request.api.v1.CommentStoreRequest;
import com.reporthub.request.api.v1.CommentUpdateRequest;
import com.reporthub.service.ICommentService;
import com.reporthub.exception.NotFoundException;
import com.reporthub.service.IReportService;
import com.reporthub.service.IUserService;
import com.reporthub.service.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ICommentServiceImpl implements ICommentService {

    @Autowired
    private ICommentRepository commentRepository;

    @Autowired private IUserService userService;
    @Autowired private IReportService reportService;

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

    @Override
    public List<CommentDTO> all() {
        return this.findAll().stream().map(CommentDTO::new).collect(Collectors.toList());
    }

    @Override
    public Response<CommentDTO> retrieveDTO(String key)  {
        Map<String, String> message = new HashMap<>();
        try {
            Comment comment = this.findByKey(key);
            if (comment == null) throw new NotFoundException("User not found");
            return new Response<>(new CommentDTO(comment), null);
        } catch (NotFoundException e) { message.put("message", "User not found"); }
        return new Response<>(null, message);
    }

    @Override
    public Response<CommentDTO> create(CommentStoreRequest request) {
        Map<String, String> message = new HashMap<>();

        try {
            return new Response<>(
                new CommentDTO(this.save(new Comment(
                    request.getContent(),
                    userService.findById(((Authenticated) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()),
                    reportService.findByKey(request.getReportId())
                ))), null);
        } catch (NotFoundException e) { message.put("message", e.getMessage()); }

        return new Response<>(new CommentDTO(null), message);
    }

    @Override
    public Response<CommentDTO> update(String key, CommentUpdateRequest request) {
        Map<String, String> message = new HashMap<>();

        try {
            Comment comment = this.findByKey(key);
            if(request.getContent() != null) comment.setContent(request.getContent());

            comment.setUpdated_at(LocalDateTime.now());
            return new Response<>(new CommentDTO(this.save(comment)), message);
        } catch (NotFoundException e) { message.put("message", e.getMessage()); }

        return new Response<>(new CommentDTO(null), message);
    }
}
