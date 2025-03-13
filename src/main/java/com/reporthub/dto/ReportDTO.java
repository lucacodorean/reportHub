package com.reporthub.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reporthub.config.AppConfig;
import com.reporthub.entity.Report;
import lombok.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ReportDTO extends PostableDTO {

    @JsonIgnore @NonNull  private String title;
    @JsonIgnore @NonNull  private String status;
    @JsonIgnore @NonNull  private List<TagDTO> tags;
    @JsonIgnore @NonNull  private Resource attachment;

    public ReportDTO(Report report) {
        super(report, "reports");

        if (report != null) {
            super.setContent(report.getContent());
            super.setUserDTO(new UserDTO(report.getUser()));
            this.title = report.getTitle();
            this.status = report.getStatus().name();
            this.tags = report.getTags().stream().map(TagDTO::new).collect(Collectors.toList());

            if(report.getAttachment() != null) {
                super.attributes.put("attachment",AppConfig.getAPIUrl() + "/" +
                        report.getAttachment().replace("\\", "/")
                );
            }
        }

        super.attributes.put("content", super.getContent());
        super.attributes.put("title", this.getTitle());
        super.attributes.put("status", this.getStatus());

        super.relationships.put("tags", this.getTags());

        super.links.put("this", AppConfig.getAPILink() + "/reports/" + this.getPostKey());
        super.links.put("parent", AppConfig.getAPILink() + "/reports/");
    }
}