package com.reporthub.controller.api.v1;

import com.reporthub.config.Rating;
import com.reporthub.service.IPostableRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/postable")
public class PostableController {

    @Autowired
    private IPostableRatingService postableRatingService;

    @PostMapping("/{key}/appreciate")
    @PreAuthorize("@authorizationService.canAppreciatePost(authentication.principal.id, #key)")
    public ResponseEntity<?> appreciate(@PathVariable String key, @RequestParam(value = "rating") Rating rating) {
       return ResponseEntity.ok(postableRatingService.appreciate(key, rating));
    }
}
