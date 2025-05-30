package com.reporthub.request.api.auth;

import com.reporthub.request.api.v1.IRequest;
import lombok.Data;
import lombok.NonNull;

@Data
final public class UserLoginRequest implements IRequest {
    @NonNull private String username;
    @NonNull private String password;
}
