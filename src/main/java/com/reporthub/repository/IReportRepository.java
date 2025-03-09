package com.reporthub.repository;

import com.reporthub.entity.Comment;
import com.reporthub.entity.Postable;
import com.reporthub.entity.Report;
import com.reporthub.entity.Report.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByStatus(Status status);

    @Query("SELECT r FROM Report r JOIN r.tags t WHERE t.name = :tagName")
    List<Report> findByTagName(@Param("tagName") String tagName);

    List<Report> findByUserId(Long userId);

    Optional<Report>
}