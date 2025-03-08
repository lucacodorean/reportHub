package com.reporthub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="users")
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User implements IModel {

    @Id
    @JsonIgnore
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "score")
    private Float score;

    @Column(name = "is_moderator")
    private Boolean isModerator;

    @Column(name = "is_banned")
    private Boolean isBanned;

    @Column(name = "key")
    private String key = generateKey(this);
}
