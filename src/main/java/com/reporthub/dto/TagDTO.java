package com.reporthub.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reporthub.entity.Tag;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO extends DTO {

    @JsonIgnore @NonNull    private Long id;
    @JsonIgnore @NonNull    private String name;

    public TagDTO(Tag tag) {
        super("tags");

        if (tag != null) {
            this.id = tag.getId();
            this.name = tag.getName();
        }

        super.attributes.put("name", this.getName());
    }
}

