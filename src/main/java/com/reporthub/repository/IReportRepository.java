package com.reporthub.repository;

import com.reporthub.entity.Report;
import com.reporthub.entity.Report.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IReportRepository extends IPostableRepository<Report> {

    @Query("SELECT r FROM Report r WHERE r.status = :status")
    List<Report> findAll(@Param("status") Status status);

    @Query("SELECT r FROM Report r WHERE r.status = :status")
    List<Report> findByStatus(@Param("status") Status status);

    @Query("SELECT r FROM Report r JOIN r.tags t WHERE t.name = :tagName")
    List<Report> findByTagName(@Param("tagName") String tagName);

    @Query("SELECT r FROM Report r WHERE r.post_key = :postKey")
    Optional<Report> findByPostKey(@Param("postKey") String postKey);

}
