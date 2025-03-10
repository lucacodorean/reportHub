package com.reporthub.dto;

import com.reporthub.dto.PostableDTO;
import com.reporthub.entity.Report;
import com.reporthub.entity.Tag;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ReportDTO extends PostableDTO {

    private String title;
    private String status;
    private List<String> tagNames;

    public ReportDTO(Report report) {
        super(report, "reports");

        if (report != null) {
            this.title = report.getTitle();
            this.status = report.getStatus().name();
            this.tagNames = report.getTags().stream()
                    .map(Tag::getName)
                    .collect(Collectors.toList());
        }

        super.attributes.put("title", this.getTitle());
        super.attributes.put("status", this.getStatus());
        super.attributes.put("tags", this.getTagNames());
    }
}
