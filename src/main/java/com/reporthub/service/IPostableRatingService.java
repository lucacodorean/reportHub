package com.reporthub.service;

import com.reporthub.entity.Postable;
import org.springframework.stereotype.Service;

public interface IPostableRatingService {
    Boolean ratePostable(Long userId, Postable postable, Boolean status);
}
