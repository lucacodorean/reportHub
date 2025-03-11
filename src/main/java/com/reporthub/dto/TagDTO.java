package com.reporthub.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reporthub.config.AppConfig;
import com.reporthub.entity.Tag;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TagDTO extends DTO {

    @JsonIgnore @NonNull    private Long id;
    @JsonIgnore @NonNull    private String name;
    @JsonIgnore @NonNull    private String modelKey;

    public TagDTO(Tag tag) {
        super("tags");

        if (tag != null) {
            this.modelKey = tag.getModelKey();
            this.id = tag.getId();
            this.name = tag.getName();
        }

        super.key = this.modelKey;
        super.attributes.put("name", this.getName());
        super.links.put("this", AppConfig.getAPILink() + "/tags/" + this.modelKey);
        super.links.put("parent", AppConfig.getAPILink() + "/tags/");
    }
}

