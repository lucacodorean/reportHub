package com.reporthub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "reports", schema = "reporthub")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report extends Postable {

    public enum Status {
        RECEIVED,
        IN_PROGRESS,
        SOLVED
    }

    @Column(nullable = false, length = 128)
    private String title;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.RECEIVED;

    @Column(name = "attachment_path")
    private String attachment;

    @ManyToMany
    @JoinTable(
            name = "report_tags",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    public Report(String content, User user, String title, List<Tag> tags) {
        super(content, user);
        this.title = title;
        this.status = Status.RECEIVED;
        this.tags = tags;
    }

    public Report(String content, User user, String title, List<Tag> tags, String attachmentPath) {
        super(content, user);
        this.title = title;
        this.status = Status.RECEIVED;
        this.tags = tags;
        this.attachment = attachmentPath;
    }
}
