package com.reporthub.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Comment extends Postable{

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;
}
