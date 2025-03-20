package com.reporthub.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Postable implements IModel {

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", length = 1024)
    private String content;

    @Column(name = "created_at",nullable = false, updatable = false)
    private LocalDateTime created_at = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "post_key")
    private String post_key = generateKey(this);

    @OneToMany(mappedBy = "postable")
    private Set<PostableRating> ratings;

    @Column(name="like_count")
    private Long like_count;

    @Column(name="dislike_count")
    private Long dislike_count;

    public Postable(String content, User user) {
        this.content = content;
        this.created_at = LocalDateTime.now();
        this.user = user;
        this.post_key = generateKey(this);
        this.like_count = 0L;
        this.dislike_count = 0L;
    }
}
