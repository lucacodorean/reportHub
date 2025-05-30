package com.reporthub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name="users", schema="reporthub")
@ToString
@Getter
@Setter
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

    @Column(name = "model_key")
    private String modelKey = generateKey(this);

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Postable> userPosts;

    @OneToMany(mappedBy = "user")
    private Set<PostableRating> ratings;

    public User(String username, String email, String password, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isModerator = false;
        this.isBanned = false;
        this.score = 0f;
    }
}
