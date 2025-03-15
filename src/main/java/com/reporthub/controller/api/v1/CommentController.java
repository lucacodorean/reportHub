package com.reporthub.controller.api.v1;

import com.reporthub.dto.CommentDTO;
import com.reporthub.entity.Comment;
import com.reporthub.entity.auth.Authenticated;
import com.reporthub.request.api.v1.CommentStoreRequest;
import com.reporthub.request.api.v1.CommentUpdateRequest;
import com.reporthub.service.ICommentService;
import com.reporthub.service.IReportService;
import com.reporthub.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    @Autowired private  ICommentService commentService;
    @Autowired private  IUserService userService;
    @Autowired private  IReportService reportService;

    @GetMapping("/")
    public ResponseEntity<List<CommentDTO>> index() {
        return ResponseEntity.status(HttpStatus.OK).body(
                commentService.findAll().stream().map(CommentDTO::new).toList()
        );
    }

    @GetMapping("/{key}")
    public ResponseEntity<CommentDTO> get(@PathVariable String key) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommentDTO(commentService.findByKey(key))
        );
    }

    @PostMapping("/")
    @PreAuthorize("@authorizationService.isConnected(authentication.principal.id)")
    public ResponseEntity<CommentDTO> create(@RequestBody CommentStoreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommentDTO(commentService.save(new Comment(
            request.getContent(),
            userService.findById(((Authenticated) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()),
            reportService.findByKey(request.getReportId())
        ))));
    }


    @PatchMapping("/{key}")
    @PreAuthorize("@authorizationService.canOperateComment(authentication.principal.id, #key)")
    public ResponseEntity<CommentDTO> update(@PathVariable String key, @RequestBody CommentUpdateRequest request) {
        Comment comment = commentService.findByKey(key);
        if(request.getContent() != null) comment.setContent(request.getContent());

        comment.setUpdated_at(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(new CommentDTO(commentService.save(comment)));
    }

    @DeleteMapping("/{key}")
    @PreAuthorize("@authorizationService.canOperateComment(authentication.principal.id, #key)")
    public ResponseEntity<?> delete(@PathVariable String key) {
        commentService.delete(commentService.findByKey(key));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
