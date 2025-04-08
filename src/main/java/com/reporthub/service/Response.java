package com.reporthub.service;

import com.reporthub.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Response<T extends DTO> {
    public T entityDTO;
    public Map<String, String> message;

    public String retrieveMessages() {
        return this.getMessage()
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));
    }
}
