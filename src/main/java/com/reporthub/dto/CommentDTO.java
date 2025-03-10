package com.reporthub.dto;

import com.reporthub.entity.Comment;
import com.reporthub.config.AppConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO extends PostableDTO {

    private String reportKey;

    public CommentDTO(Comment comment) {
        super(comment, "comments");

        if (comment != null) {
            this.reportKey = comment.getReport().getPost_key();
        }

        super.attributes.put("reportKey", this.getReportKey());

        super.links.put("report", AppConfig.getAPILink() + "/reports/" + this.getReportKey());
    }
}
