package com.fortna.hackathon.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import com.fortna.hackathon.config.TokenProvider;
import com.fortna.hackathon.dto.LoginRequest;
import com.fortna.hackathon.dto.LoginResponse;
import com.fortna.hackathon.dto.UserDto;
import com.fortna.hackathon.entity.User;
import com.fortna.hackathon.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody LoginRequest loginUser) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        userService.updateAccessToken(token, loginUser.getUsername());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserDto user) {
        User entity = userService.findOne(user.getUsername());
        if (entity == null) {
            userService.save(user);
            return ResponseEntity.status(HttpStatus.OK).body("Register successfully!");
        } else
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already existed!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ResponseEntity<?> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("This page is for admin only!");
    }

    @RequestMapping(value = "/user/logout", method = RequestMethod.GET)
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            userService.updateAccessToken("", authentication.getName());
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Logout successfully!");
    }

}
