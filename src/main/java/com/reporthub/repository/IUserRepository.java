package com.reporthub.repository;

import com.reporthub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    @Query("SELECT usr FROM User usr WHERE usr.modelKey = :key")
    Optional<User> findByKey(@Param("key") String key);

    @Query("SELECT usr FROM User usr WHERE usr.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT usr FROM User usr WHERE usr.username = :username")
    User findByUsername(@Param("username") String username);
}
