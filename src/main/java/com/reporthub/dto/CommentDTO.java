package com.reporthub.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reporthub.entity.Comment;
import com.reporthub.config.AppConfig;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Data
public class CommentDTO extends PostableDTO {

    @JsonIgnore @NonNull private ReportDTO reportDTO;

    public CommentDTO(Comment comment) {
        super(comment, "comments");

        super.links.put("this",    AppConfig.getAPILink() + "/comments/" + this.key);
        super.links.put("parent",  AppConfig.getAPILink() + "/comments/");
    }
}
