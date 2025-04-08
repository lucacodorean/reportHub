package com.reporthub.request.api.v1;

import lombok.Getter;
import lombok.NonNull;

@Getter
final public class TagStoreRequest implements IRequest {
    @NonNull private String name;
}
