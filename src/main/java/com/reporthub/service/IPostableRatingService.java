package com.reporthub.service;

import com.reporthub.config.Rating;
import com.reporthub.entity.Postable;

import java.util.Map;

public interface IPostableRatingService {
    Boolean ratePostable(Long userId, Postable postable, Boolean status);
    Map<String, String> appreciate(String key, Rating rating);
}
