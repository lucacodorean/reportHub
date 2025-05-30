package com.reporthub.request.api.v1;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
final public class CommentUpdateRequest implements IRequest {
    @Nullable private String content;
}
