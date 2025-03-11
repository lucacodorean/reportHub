package com.reporthub.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reporthub.config.AppConfig;
import com.reporthub.entity.Postable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class PostableDTO extends DTO {

    @JsonIgnore @NonNull    private String content;
    @JsonIgnore @NonNull    private LocalDateTime createdAt;
    @JsonIgnore             private LocalDateTime updatedAt;
    @JsonIgnore @NonNull    private String postKey;
    @JsonIgnore             private Long likeCount;
    @JsonIgnore             private Long dislikeCount;
    @JsonIgnore             private UserDTO userDTO;

    public PostableDTO(Postable postable, String type) {
        super(type);

        if (postable != null) {
            this.content = postable.getContent();
            this.createdAt = postable.getCreated_at();
            this.updatedAt = postable.getUpdated_at();
            this.postKey = postable.getPost_key();
            this.likeCount = postable.getLike_count();
            this.dislikeCount = postable.getDislike_count();
            this.userDTO = new UserDTO(postable.getUser());
        }

        super.key = this.postKey;
        super.attributes.put("content", this.getContent());
        super.attributes.put("createdAt", this.getCreatedAt());
        super.attributes.put("updatedAt", this.getUpdatedAt());
        super.attributes.put("likeCount", this.getLikeCount());
        super.attributes.put("dislikeCount", this.getDislikeCount());
        super.attributes.put("userDto", this.getUserDTO());

        super.relationships.put("userDto", this.getUserDTO());

        super.links.put("this", AppConfig.getAPILink() + "/posts/" + this.getPostKey());
        super.links.put("parent", AppConfig.getAPILink() + "/posts/");
    }
}
