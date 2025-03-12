package com.reporthub.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reporthub.config.AppConfig;
import com.reporthub.dto.PostableDTO;
import com.reporthub.entity.Report;
import com.reporthub.entity.Tag;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Data
public class ReportDTO extends PostableDTO {

    @JsonIgnore @NonNull  private String title;
    @JsonIgnore @NonNull  private String status;
    @JsonIgnore @NonNull  private List<TagDTO> tags;

    public ReportDTO(Report report) {
        super(report, "reports");

        if (report != null) {
            super.setContent(report.getContent());
            super.setUserDTO(new UserDTO(report.getUser()));
            this.title = report.getTitle();
            this.status = report.getStatus().name();
            this.tags = report.getTags().stream().map(TagDTO::new).collect(Collectors.toList());
        }

        super.attributes.put("content", super.getContent());
        super.attributes.put("title", this.getTitle());
        super.attributes.put("status", this.getStatus());

        super.relationships.put("tags", this.getTags());

        super.links.put("this", AppConfig.getAPILink() + "/reports/" + this.getPostKey());
        super.links.put("parent", AppConfig.getAPILink() + "/reports/");
    }
}