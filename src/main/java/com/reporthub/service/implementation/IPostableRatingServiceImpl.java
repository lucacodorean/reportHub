package com.reporthub.service.implementation;

import com.reporthub.config.Rating;
import com.reporthub.entity.*;
import com.reporthub.entity.auth.Authenticated;
import com.reporthub.exception.NotFoundException;
import com.reporthub.repository.IPostableRatingRepository;
import com.reporthub.repository.IUserRepository;
import com.reporthub.service.ICommentService;
import com.reporthub.service.IPostableRatingService;
import com.reporthub.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class IPostableRatingServiceImpl implements IPostableRatingService {

    @Autowired private IReportService reportService;
    @Autowired private ICommentService commentService;

    @Autowired private IUserRepository userRepository;
    @Autowired private IPostableRatingRepository postableRatingRepository;

    private void updateUserScore(Postable postable, Boolean status) {
        User owner = postable.getUser();
        Boolean statusNull = status == null;
        if(postable instanceof Comment) {
            userRepository.findById(
                ((Authenticated) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()
            ).ifPresent(auth -> {
                auth.setScore(auth.getScore() + (!statusNull && status ? 0.0f : -1.5f));
                userRepository.save(auth);
            });

            owner.setScore(postable.getUser().getScore() + (!statusNull && status ? 5.0f : -2.5f));

        }

        else owner.setScore(postable.getUser().getScore() + (!statusNull && status ? 2.5f : -1.5f));
        userRepository.save(owner);
    }

    @Override
    public Boolean ratePostable(Long userId, Postable postable, Boolean status) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) return false;

        System.out.println(postable.getId());
        PostableRatingKey key = new PostableRatingKey(userId, postable.getId());
        PostableRating rating = new PostableRating(key, user, postable, status);
        postableRatingRepository.save(rating);


        user.getRatings().remove(rating);
        boolean returnStatus = true;
        if(status != null) returnStatus = user.getRatings().add(rating);

        postableRatingRepository.setFeedbackCounters(
            postable.getId(),
            postable.getRatings().stream().filter(r ->  Boolean.TRUE.equals(r.getStatus())).count() ,
            postable.getRatings().stream().filter(r ->  Boolean.FALSE.equals(r.getStatus())).count()
        );

        this.updateUserScore(postable, status);
        userRepository.save(user);
        return returnStatus;
    }

    public Map<String, String> appreciate(String key, Rating rating) {
        Map<String, String> message = new HashMap<>();
        try {
            Postable postable;
            if(key.contains("com_")) postable = commentService.findByKey(key);
            else postable = reportService.findByKey(key);

            Boolean status = false;
            if(rating == Rating.NULL) status = null;
            if(rating == Rating.LIKE) status = true;
            if(rating == Rating.DISLIKE) status = false;

            Long userId = ((Authenticated) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

            message.put("status", this.ratePostable(userId, postable, status) ? "true" : "false");
        } catch (NotFoundException ex) { message.put("message", ex.getMessage()); }

        return message;
    }
}
