package com.reporthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.reporthub.entity.Postable;

import java.util.List;

@NoRepositoryBean
public interface IPostableRepository<T extends Postable> extends JpaRepository<T, Long> {

    @Modifying
    @Query("UPDATE #{#entityName} p SET p.like_count = :likes WHERE p.id = :postId")
    void updateLikes(@Param("postId") Long postId, @Param("like_count") Long likes);

    @Modifying
    @Query("UPDATE #{#entityName} p SET p.dislike_count = :dislikes WHERE p.id = :postId")
    void updateDislikesById(@Param("postId") Long postId, @Param("dislike_count") Long dislikes);

    @Query("SELECT p FROM #{#entityName} p WHERE p.user.id = :userId")
    List<T> findByUserId(@Param("userId") Long userId);
}
