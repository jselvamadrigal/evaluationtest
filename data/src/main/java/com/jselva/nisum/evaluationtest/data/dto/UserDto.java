package com.jselva.nisum.evaluationtest.data.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private Long id;

    private String name;

    @NotEmpty(message = "Email requerido")
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "{email.invalid}")
    private String email;

    @NotNull(message = "${value.not.null}")
    @NotEmpty(message = "${value.not.empty}")
    private String password;

    private String token;

    private boolean isActive;

    private LocalDateTime lastLogin;

    private LocalDateTime created;

    private LocalDateTime modified;

    private List<PhoneDto> phones;

    private List<RoleDto> roles;

    public UserDto() {
    }

    public UserDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserDto(String name, String email, String password, List<PhoneDto> phones) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phones = phones;
    }

    public UserDto(Long id, String name, String email, String password, String token, boolean active,
                   LocalDateTime lastLogin, LocalDateTime created, LocalDateTime modified, List<PhoneDto> phones,
                   List<RoleDto> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.token = token;
        this.isActive = active;
        this.lastLogin = lastLogin;
        this.created = created;
        this.modified = modified;
        this.phones = phones;
        this.roles = roles;
    }

    public Long getId() {
        return this.id;
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

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public LocalDateTime getLastLogin() {
        return this.lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDateTime getCreated() {
        return this.created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return this.modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public List<PhoneDto> getPhones() {
        return this.phones;
    }

    public void setPhones(List<PhoneDto> phones) {
        this.phones = phones;
    }

    public List<RoleDto> getRoles() {
        return this.roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;

        var userDto = (UserDto) o;

        if (!Objects.equals(id, userDto.id)) return false;
        if (!Objects.equals(name, userDto.name)) return false;
        return Objects.equals(email, userDto.email);
    }

    @Override
    public int hashCode() {
        var result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}