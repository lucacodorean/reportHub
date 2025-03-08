package com.reporthub.dto;

import com.reporthub.entity.User;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserDTO {
    @NonNull private String username;
    @NonNull private String phoneNumber;
    @NonNull private String email;
    @NonNull private Float score;
    @NonNull private String key;

    public UserDTO(User user) {
        if(user != null) {
            this.email          = user.getEmail();
            this.phoneNumber    = user.getPhoneNumber();
            this.username       = user.getUsername();
            this.score          = user.getScore();
            this.key            = user.getKey();
        }
    }
}
