package com.reporthub.repository;

import com.reporthub.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT tag FROM Tag tag WHERE tag.modelKey = :key")
    Optional<Tag> findByKey(@Param("key") String key);
}
