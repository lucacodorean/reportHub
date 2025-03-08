package com.reporthub.dto.auth;

import lombok.Data;
import lombok.NonNull;

@Data
public class RegisterRequest {
    @NonNull private String email;
    @NonNull private String password;
    @NonNull private String username;
    @NonNull private String phoneNumber;
}
