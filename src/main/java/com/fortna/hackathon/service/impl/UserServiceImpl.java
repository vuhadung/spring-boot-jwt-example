package com.fortna.hackathon.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fortna.hackathon.dao.UserDao;
import com.fortna.hackathon.dto.UserDto;
import com.fortna.hackathon.entity.Role;
import com.fortna.hackathon.entity.User;
import com.fortna.hackathon.entity.UserRole;
import com.fortna.hackathon.service.RoleService;
import com.fortna.hackathon.service.UserService;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserDao userDao;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole().getName()));
        });
        return authorities;
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        userDao.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public User findOne(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User save(UserDto user) {

        User nUser = user.getUserFromDto();
        nUser.setPassword(new MessageDigestPasswordEncoder("MD5").encode(user.getPassword()));
        Set<UserRole> roleSet = new HashSet<>();

        Role role = roleService.findByName("USER");
        UserRole userRole = new UserRole();
        userRole.setUser(nUser);
        userRole.setRole(role);
        roleSet.add(userRole);

        nUser.setRoles(roleSet);
        return userDao.save(nUser);
    }
}
