package com.reporthub.service.implementation;

import com.reporthub.entity.*;
import com.reporthub.entity.auth.Authenticated;
import com.reporthub.repository.IPostableRatingRepository;
import com.reporthub.repository.IUserRepository;
import com.reporthub.service.IPostableRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class IPostableRatingServiceImpl implements IPostableRatingService {

    @Autowired private IUserRepository userRepository;

    @Autowired private IPostableRatingRepository postableRatingRepository;

    private void updateUserScore(Postable postable, Boolean status) {
        User owner = postable.getUser();
        if(postable instanceof Comment) {
            userRepository.findById(
                ((Authenticated) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()
            ).ifPresent(auth -> {
                auth.setScore(auth.getScore() + (status ? 0.0f : -1.5f));
                userRepository.save(auth);
            });

            owner.setScore(postable.getUser().getScore() + (status ? 5.0f : -2.5f));
        }

        else owner.setScore(postable.getUser().getScore() + (status ? 2.5f : -1.5f));
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
}
