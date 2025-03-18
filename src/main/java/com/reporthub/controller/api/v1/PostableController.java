package com.reporthub.controller.api.v1;

import com.reporthub.entity.Postable;
import com.reporthub.entity.auth.Authenticated;
import com.reporthub.service.ICommentService;
import com.reporthub.service.IPostableRatingService;
import com.reporthub.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


enum Rating {
    LIKE,
    DISLIKE,
    NULL
}

@RestController
@RequestMapping("/api/v1/postable")
public class PostableController {

    @Autowired private IReportService           reportService;
    @Autowired private ICommentService          commentService;
    @Autowired private IPostableRatingService   postableRatingService;

    @PostMapping("/{key}/appreciate")
    @PreAuthorize("@authorizationService.canAppreciatePost(authentication.principal.id, #key)")
    public ResponseEntity<?> appreciate(@PathVariable String key, @RequestParam(value = "rating") Rating rating) {
        Postable postable;
        if(key.contains("com_")) postable = commentService.findByKey(key);
        else postable = reportService.findByKey(key);

        Boolean status = false;
        if(rating == Rating.NULL) status = null;
        if(rating == Rating.LIKE) status = true;
        if(rating == Rating.DISLIKE) status = false;

        Long userId = ((Authenticated) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Map<String, Boolean> message = new HashMap<>();
        message.put("status", postableRatingService.ratePostable(userId, postable, status));
        return ResponseEntity.ok(message);
    }
}
