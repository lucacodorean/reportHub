package com.reporthub.controller.api.v1;

import com.reporthub.dto.TagDTO;
import com.reporthub.request.api.v1.TagStoreRequest;
import com.reporthub.service.ITagService;
import com.reporthub.exception.NotFoundException;
import com.reporthub.service.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags/")
public class TagController {


    @Autowired private ITagService tagService;

    @GetMapping("/")
    @PreAuthorize("@authorizationService.isConnected(authentication.principal.id)")
    public ResponseEntity<List<TagDTO>> index() {
        return ResponseEntity.status(HttpStatus.OK).body(tagService.all());
    }

    @GetMapping("/{key}")
    public ResponseEntity<?> get(@PathVariable String key) {
        Response<TagDTO> myResponse = tagService.retrieveDTO(key);
        if(myResponse.getEntityDTO() != null) return ResponseEntity.ok(myResponse.getEntityDTO());
        else return ResponseEntity.badRequest().body(myResponse.retrieveMessages());
    }

    @PostMapping("/")
    @PreAuthorize("@authorizationService.isConnected(authentication.principal.id)")
    public ResponseEntity<TagDTO> create(@RequestBody TagStoreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.create(request).getEntityDTO());
    }

    @DeleteMapping("/{key}")
    @PreAuthorize("@authorizationService.isAdmin(authentication.principal.id)")
    public ResponseEntity<Boolean> delete(@PathVariable String key) {
        try {
            Boolean status = tagService.delete(tagService.findByKey(key));
            return ResponseEntity.status(HttpStatus.OK).body(status);
        } catch (NotFoundException ex) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); }
    }
}
