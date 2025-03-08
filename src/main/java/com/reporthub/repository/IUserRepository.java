package com.reporthub.repository;

import com.reporthub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    @Query("SELECT usr FROM User usr WHERE usr.key = :key")
    Optional<User> findByKey(@Param("key") String key);
}
