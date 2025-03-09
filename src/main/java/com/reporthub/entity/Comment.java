package com.reporthub.entity;


import jakarta.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Comment extends Postable{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;
}
