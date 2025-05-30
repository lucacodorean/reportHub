package com.reporthub.repository;

import com.reporthub.entity.PostableRating;
import com.reporthub.entity.PostableRatingKey;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IPostableRatingRepository extends JpaRepository<PostableRating, PostableRatingKey> {

    @Modifying
    @Transactional
    @Query("UPDATE Postable p SET p.like_count = :likes, p.dislike_count = :dislikes WHERE p.id = :postId")
    void setFeedbackCounters(@Param("postId") Long postId, @Param("likes") Long likes, @Param("dislikes") Long dislikes);
}
