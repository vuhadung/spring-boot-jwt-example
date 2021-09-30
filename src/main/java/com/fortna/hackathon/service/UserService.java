package com.fortna.hackathon.service;

import java.util.List;

import com.fortna.hackathon.dto.UserDto;
import com.fortna.hackathon.entity.User;

public interface UserService {
    User save(UserDto user);
    List<User> findAll();
    User findOne(String username);
    void updateAccessToken(String token, String username);
    String getUserAvatar(String username, boolean isCompressed);
    boolean changePassword(String username, String oldPwd, String newPwd);
}
