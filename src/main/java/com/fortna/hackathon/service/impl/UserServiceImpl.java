package com.fortna.hackathon.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fortna.hackathon.dao.UserDao;
import com.fortna.hackathon.dto.UserDto;
import com.fortna.hackathon.entity.Role;
import com.fortna.hackathon.entity.User;
import com.fortna.hackathon.entity.UserRole;
import com.fortna.hackathon.service.RoleService;
import com.fortna.hackathon.service.UserService;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserDao userDao;

    /**
     * {@inheritDoc} <br/>
     * Lazy loading requires a transaction to keep Hibernate session proxy opened
     * <br/>
     * More information, see <a href =
     * "https://stackoverflow.com/questions/48891079/hibernate-failed-to-lazily-initialize-a-collection-of-role-could-not-initiali">
     * this link </a>
     * 
     */
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole().getName()));
        });

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }

    @Transactional
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        userDao.findAll().iterator().forEachRemaining((u) -> {
            if (u.getRoles().stream().anyMatch(role -> "USER".equals(role.getRole().getName())))
                list.add(u);
        });
        return list;
    }

    @Override
    public User findOne(String username) {
        return userDao.findByUsername(username);
    }

    @SuppressWarnings("deprecation")
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

    @Override
    @Transactional
    public void updateAccessToken(String token, String username) {
        userDao.updateAccessTokenByUsername(token, username);
    }

    @SuppressWarnings("restriction")
    @Override
    @Transactional
    public String getUserAvatar(String username) {
        User user = userDao.findByUsername(username);
        if (user.getAvatar() != null) {
            return Base64.encode(user.getAvatar());
        }
        return new String();
    }

    @SuppressWarnings("deprecation")
    @Override
    @Transactional
    public boolean changePassword(String username, String oldPwd, String newPwd) {
        User user = userDao.findByUsername(username);
        PasswordEncoder encoder = new MessageDigestPasswordEncoder("MD5");
        if (!encoder.matches(oldPwd, user.getPassword()))
            return false;
        user.setPassword(encoder.encode(newPwd));
        user.setAccessToken("");
        user.setUpdatedDate(new Date());
        userDao.save(user);
        return true;
    }

}
