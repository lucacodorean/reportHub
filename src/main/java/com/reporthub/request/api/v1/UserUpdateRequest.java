package com.reporthub.request.api.v1;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class UserUpdateRequest {
    @Nullable private String username;
    @Nullable private String phoneNumber;
    @Nullable private Float score;
    @Nullable private String email;
    @Nullable private String password;
    @Nullable private Boolean banned;
}
