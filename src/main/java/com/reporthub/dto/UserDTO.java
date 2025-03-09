package com.reporthub.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reporthub.config.AppConfig;
import com.reporthub.entity.User;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserDTO extends DTO {

    @JsonIgnore @NonNull    private String username;
    @JsonIgnore @NonNull    private String phoneNumber;
    @JsonIgnore @NonNull    private String email;
    @JsonIgnore @NonNull    private Float score;
    @JsonIgnore @NonNull    private String modelKey;

    public UserDTO(User user) {
        super("users");

        if(user != null) {
            this.email          = user.getEmail();
            this.phoneNumber    = user.getPhoneNumber();
            this.username       = user.getUsername();
            this.score          = user.getScore();
            this.modelKey       = user.getModelKey();
        }

        super.key = this.modelKey;
        super.attributes.put("email", this.getEmail());
        super.attributes.put("username", this.getUsername());
        super.attributes.put("phoneNumber", this.getPhoneNumber());
        super.attributes.put("score", this.getScore());

        super.links.put("this",    AppConfig.getAPILink() + "/users/" + this.getModelKey());
        super.links.put("parent",  AppConfig.getAPILink() + "/users/");
    }
}
