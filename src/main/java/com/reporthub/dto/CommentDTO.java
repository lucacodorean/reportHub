package com.reporthub.dto;

import com.reporthub.entity.Comment;
import com.reporthub.config.AppConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO extends PostableDTO {

    private ReportDTO reportDTO;

    public CommentDTO(Comment comment) {
        super(comment, "comments");

        if (comment != null) {
            this.reportDTO = new ReportDTO(comment.getReport());
        }

        super.attributes.put("reportKey", this.getReportDTO().getPostKey());

        super.links.put("this",    AppConfig.getAPILink() + "/comments/" + this);
        super.links.put("parent",  AppConfig.getAPILink() + "/comments/");
    }
}
