package com.fortna.hackathon.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fortna.hackathon.entity.User;

public class UserDto {

    @NotEmpty(message = "Username can not be empty!")
    @Size(max = 32, message = "Username can not exceed 32 characters!")
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9_]*", message = "Username is invalid!")
    private String username;

    @NotEmpty(message = "Display name can not be empty!")
    @Size(max = 32, message = "Display name can not exceed 32 characters!")
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9_\\s]*", message = "Display name is invalid!")
    private String nickname;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public User getUserFromDto() {
        User user = new User();
        user.setUsername(username);
        user.setDisplayName(nickname);
        user.setPassword(password);
        return user;
    }

}