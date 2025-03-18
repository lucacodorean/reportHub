package com.reporthub.repository;

import com.reporthub.entity.PostableRating;
import com.reporthub.entity.PostableRatingKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPostableRatingRepository extends JpaRepository<PostableRating, PostableRatingKey> { }
