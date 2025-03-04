package com.example.JWTdemo.controller;

import com.example.JWTdemo.entity.AuthRequest;
import com.example.JWTdemo.entity.UserInfo;
import com.example.JWTdemo.service.JwtService;
import com.example.JWTdemo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/users")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public UserInfo getUser(@PathVariable int userId) {
        return service.getUserById(userId);
    }

    @PutMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String updateUser(@PathVariable int userId, @RequestBody UserInfo userInfo) {
        return service.updateUser(userId, userInfo);
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String deleteUser(@PathVariable int userId) {
        return service.deleteUser(userId);
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    // Thêm các endpoint cho posts theo ví dụ đã đề cập
}
