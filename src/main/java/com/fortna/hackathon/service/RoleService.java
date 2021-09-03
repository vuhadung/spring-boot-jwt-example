package com.fortna.hackathon.service;

import com.fortna.hackathon.entity.Role;

public interface RoleService {
    Role findByName(String name);
}
