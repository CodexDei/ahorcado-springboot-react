package com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response;

import java.util.List;

import com.codexdei.crudjuegoahorcado.ahorcado.entities.Role;

public class UserResponseDto {

    private Long id;
    private String username;
    private List<Role> roles;
    private boolean enabled;

    public UserResponseDto() {
    }

    public UserResponseDto(
            Long id,
            String username,
            List<Role> roles,
            boolean enabled) {

        this.id = id;
        this.username = username;
        this.roles = roles;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
