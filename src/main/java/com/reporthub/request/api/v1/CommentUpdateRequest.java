package com.reporthub.request.api.v1;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class CommentUpdateRequest {
    @Nullable private String content;
}
