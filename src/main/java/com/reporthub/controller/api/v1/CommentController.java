package com.reporthub.controller.api.v1;

import com.reporthub.dto.CommentDTO;
import com.reporthub.request.api.v1.CommentStoreRequest;
import com.reporthub.request.api.v1.CommentUpdateRequest;
import com.reporthub.service.ICommentService;
import com.reporthub.exception.NotFoundException;
import com.reporthub.service.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    @Autowired private  ICommentService commentService;

    @GetMapping("/")
    public ResponseEntity<List<CommentDTO>> index() {
        return ResponseEntity.status(HttpStatus.OK).body(
                commentService.findAll().stream().map(CommentDTO::new).toList()
        );
    }

    @GetMapping("/{key}")
    public ResponseEntity<CommentDTO> get(@PathVariable String key) {
        try {
            CommentDTO temp = new CommentDTO(commentService.findByKey(key));
            if(temp.key == null) return ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.OK).body(temp);
        } catch (NotFoundException e) { return ResponseEntity.notFound().build(); }
    }

    @PostMapping("/")
    @PreAuthorize("@authorizationService.isConnected(authentication.principal.id)")
    public ResponseEntity<?> create(@RequestBody CommentStoreRequest request) {
        Response<CommentDTO> myResponse = commentService.create(request);
        if(myResponse.getEntityDTO() != null) return ResponseEntity.ok(myResponse.getEntityDTO());
        else return ResponseEntity.badRequest().body(myResponse.retrieveMessages());
    }


    @PatchMapping("/{key}")
    @PreAuthorize("@authorizationService.canOperateComment(authentication.principal.id, #key)")
    public ResponseEntity<?> update(@PathVariable String key, @RequestBody CommentUpdateRequest request) {
        Response<CommentDTO> myResponse = commentService.update(key, request);
        if(myResponse.getEntityDTO() != null) return ResponseEntity.ok(myResponse.getEntityDTO());
        else return ResponseEntity.badRequest().body(myResponse.retrieveMessages());
    }

    @DeleteMapping("/{key}")
    @PreAuthorize("@authorizationService.canOperateComment(authentication.principal.id, #key)")
    public ResponseEntity<?> delete(@PathVariable String key) {
        try {
            commentService.delete(commentService.findByKey(key));
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NotFoundException e) { return ResponseEntity.notFound().build(); }
    }
}
