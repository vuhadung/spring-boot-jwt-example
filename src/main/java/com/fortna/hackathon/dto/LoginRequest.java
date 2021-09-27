package com.fortna.hackathon.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginRequest {

    @NotEmpty(message = "Username can not be empty!")
    @Size(max = 32, message = "Username can not exceed 32 characters!")
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9_]*", message = "Username is invalid!")
    private String username;
    
    @NotEmpty(message = "Password can not be empty!")
    private String password;

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
}