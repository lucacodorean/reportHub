package com.reporthub.dto.auth;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginRequest {
    @NonNull private String username;
    @NonNull private String password;
}
