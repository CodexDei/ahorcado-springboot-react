package com.codexdei.crudjuegoahorcado.ahorcado.security.dto;

import java.util.List;

import com.codexdei.crudjuegoahorcado.ahorcado.entities.Role;

public class JwtResponseDto {

    private String token;
    private String username;
    private List<Role> roles;

    public JwtResponseDto() {
    }

    public JwtResponseDto(String token, String username, List<Role> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

}
