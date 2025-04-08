package com.reporthub.request.api.v1;

import com.reporthub.entity.Report;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
final public class ReportStoreRequest implements IRequest {
    @NonNull private String title;
    @NonNull private Report.Status status;
    @NonNull private String content;
    @NonNull private List<String> tags;
}
