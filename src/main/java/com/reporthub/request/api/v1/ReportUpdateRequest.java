package com.reporthub.request.api.v1;

import com.reporthub.entity.Report;
import jakarta.annotation.Nullable;
import lombok.*;

import java.util.List;

@Getter
public class ReportUpdateRequest {
    @Nullable private String title;
    @Nullable private Report.Status status;
    @Nullable private String content;
    @Nullable private List<String> tags;
    @Nullable private Long dislikeCount;
    @Nullable private Long likeCount;
}
