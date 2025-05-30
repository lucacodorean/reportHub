package com.reporthub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostableRatingKey implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "postable_id")
    private Long postableId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostableRatingKey that = (PostableRatingKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(postableId, that.postableId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, postableId);
    }
}
