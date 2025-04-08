package com.reporthub.request.api.v1;

import lombok.Getter;
import lombok.NonNull;

@Getter
final public class CommentStoreRequest implements IRequest {
    @NonNull private String content;
    @NonNull private String reportId;
}
