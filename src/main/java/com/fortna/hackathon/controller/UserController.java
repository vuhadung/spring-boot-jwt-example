package com.fortna.hackathon.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import com.fortna.hackathon.aspect.SwitchOff;
import com.fortna.hackathon.dto.AppResponse;
import com.fortna.hackathon.dto.ChangePasswordDto;
import com.fortna.hackathon.dto.LoginRequest;
import com.fortna.hackathon.dto.UserDto;
import com.fortna.hackathon.entity.User;
import com.fortna.hackathon.security.TokenProvider;
import com.fortna.hackathon.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @PreAuthorize("permitAll()")
    @PostMapping(value = "/auth/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generateToken(@Valid @RequestBody LoginRequest loginUser) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        userService.updateAccessToken(token, loginUser.getUsername());
        return ResponseEntity.ok(new AppResponse(null, token));
    }

    @SwitchOff(epochTime = "1633957200")
    @PreAuthorize("permitAll()")
    @PostMapping(value = "/auth/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserDto user) {
        User entity = userService.findOne(user.getUsername());
        if (entity == null) {
            userService.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, "Register successfully!"));
        } else
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new AppResponse("User already existed!", null));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> ping() {
        return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, "This page is for admin only!"));
    }

    @GetMapping(value = "/auth/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            userService.updateAccessToken("", authentication.getName());
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, "Logout successfully!"));
    }

    @GetMapping(value = "/user/avatar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserAvatar(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(name = "compress", required = true) String compress) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isCompressed;
        if ("0".equals(compress)) {
            isCompressed = false;
        } else if ("1".equals(compress)) {
            isCompressed = true;
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppResponse(null, "Invalid params!"));
        }
        String data = userService.getUserAvatar(authentication.getName(), isCompressed);
        return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, data));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, userService.findAll()));
    }

    @PostMapping(value = "/auth/change-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(HttpServletRequest request, HttpServletResponse response,
            @Valid @RequestBody ChangePasswordDto changePasswordDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (userService.changePassword(authentication.getName(), changePasswordDto.getOldPassword(),
                    changePasswordDto.getNewPassword())) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new AppResponse(null, "Change password successfully!"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AppResponse("Error while changing user password", null));
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AppResponse("Error while changing user password", null));
        }
    }
}
