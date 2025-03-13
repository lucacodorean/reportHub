package com.reporthub.request.api.v1;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class CommentStoreRequest {
    @NonNull private String content;
    @NonNull private String reportId;
}
