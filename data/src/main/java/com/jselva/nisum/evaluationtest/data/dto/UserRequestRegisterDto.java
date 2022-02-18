package com.jselva.nisum.evaluationtest.data.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequestRegisterDto extends UserDto {
    @JsonCreator
    public UserRequestRegisterDto(
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password) {
        super(name, email, password);
    }
}
