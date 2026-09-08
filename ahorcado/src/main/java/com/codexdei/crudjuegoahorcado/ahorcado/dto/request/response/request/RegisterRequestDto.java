package com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response.request;

import jakarta.validation.constraints.NotBlank;

public class RegisterRequestDto {

    @NotBlank 
    private String username;
    @NotBlank 
    private String password;
    @NotBlank 
    private boolean admin;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

}
