package com.reporthub.repository;

import com.reporthub.entity.Comment;
import com.reporthub.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentRepository extends IPostableRepository<Comment> {

    @Query
    List<Comment> findByReport(Report report);

}
