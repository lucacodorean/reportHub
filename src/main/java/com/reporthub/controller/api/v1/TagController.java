package com.reporthub.controller.api.v1;

import com.reporthub.dto.TagDTO;
import com.reporthub.entity.Tag;
import com.reporthub.request.api.v1.TagStoreRequest;
import com.reporthub.service.ITagService;
import com.reporthub.singleton.ServiceSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags/")
public class TagController {


    @Autowired
    private final ITagService tagService = ServiceSingleton.getTagService();

    @GetMapping("/")
    @PreAuthorize("@authorizationService.isConnected(authentication.principal.id)")
    public ResponseEntity<List<TagDTO>> index() {
        return ResponseEntity.status(HttpStatus.OK).body(
                tagService.findAll().stream().map(TagDTO::new).toList()
        );
    }

    @GetMapping("/{key}")
    public ResponseEntity<TagDTO> get(@PathVariable String key) {
        return ResponseEntity.status(HttpStatus.OK).body(new TagDTO(tagService.findByKey(key)));
    }

    @PostMapping("/")
    @PreAuthorize("@authorizationService.isConnected(authentication.principal.id)")
    public ResponseEntity<TagDTO> create(@RequestBody TagStoreRequest request) {
        Tag tag = new Tag();
        tag.setName(request.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(new TagDTO(tagService.save(tag)));
    }

    @DeleteMapping("/{key}")
    @PreAuthorize("@authorizationService.isAdmin(authentication.principal.id)")
    public ResponseEntity<Boolean> delete(@PathVariable String key) {
        Boolean status = tagService.delete(tagService.findByKey(key));
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }
}
