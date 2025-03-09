package com.reporthub.repository;

import com.reporthub.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.report.id = :reportId")
    List<Comment> findCommentsByReportId(@Param("reportId") Long reportId);

    // Find all comments by a user (can also be found via PostableRepository)
    List<Comment> findByUserId(Long userId);
}
