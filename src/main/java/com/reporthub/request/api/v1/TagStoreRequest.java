package com.reporthub.request.api.v1;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class TagStoreRequest {
    @NonNull private String name;
}
