package com.reporthub.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Comment extends Postable{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    public Comment(String content, User user, Report report) {
        super(content, user);
        this.report = report;
    }
}
