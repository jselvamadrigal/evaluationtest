package com.jselva.nisum.evaluationtest.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDto {

    @JsonIgnore
    private Long id;

    @NotNull(message = "{value.not.null}")
    @NotEmpty(message = "{value.not.empty}")
    private String name;

    private String description;

    public RoleDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public RoleDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleDto)) return false;

        RoleDto roleDto = (RoleDto) o;

        if (!name.equals(roleDto.name)) return false;
        return description.equals(roleDto.description);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }
}