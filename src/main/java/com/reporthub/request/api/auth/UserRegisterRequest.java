package com.reporthub.request.api.auth;

import com.reporthub.request.api.v1.IRequest;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserRegisterRequest implements IRequest {
    @NonNull private String email;
    @NonNull private String password;
    @NonNull private String username;
    @NonNull private String phoneNumber;
}
