package com.reporthub.repository;

import com.reporthub.entity.Comment;
import com.reporthub.entity.Report;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICommentRepository extends IPostableRepository<Comment> {

    @Query
    List<Comment> findByReport(Report report);

    @Query("SELECT comment FROM Comment comment WHERE comment.post_key = :postKey")
    Optional<Comment> findByKey(@Param("postKey") String postKey);
}
