package com.reporthub.service.implementation;

import com.reporthub.entity.Postable;
import com.reporthub.entity.PostableRating;
import com.reporthub.entity.PostableRatingKey;
import com.reporthub.entity.User;
import com.reporthub.repository.IPostableRatingRepository;
import com.reporthub.repository.IUserRepository;
import com.reporthub.service.IPostableRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IPostableRatingServiceImpl implements IPostableRatingService {

    @Autowired private IUserRepository userRepository;

    @Autowired private IPostableRatingRepository postableRatingRepository;

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

        userRepository.save(user);
        return returnStatus;
    }
}
