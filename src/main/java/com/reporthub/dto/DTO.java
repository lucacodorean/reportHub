package com.reporthub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class DTO {

    @JsonProperty("id")             public String key;
    @JsonProperty("type" )          public String type;
    @JsonProperty("attributes")     public Map<String, Object> attributes;
    @JsonProperty("relationships")  public Map<String, Object> relationships;
    @JsonProperty("links")          public Map<String, String> links;

    public DTO() { }

    public DTO(String type) {
        this.type = type;
        this.attributes = new HashMap<>();
        this.links = new HashMap<>();
        this.relationships = new HashMap<>();
    }
}
