package com.example.JWTdemo.service;

import com.example.JWTdemo.entity.UserInfo;
import com.example.JWTdemo.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByEmail(username); // Assuming 'email' is used as username

        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public String addUser(UserInfo userInfo) {
        // Encode password before saving the user
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added Successfully";
    }

    public UserInfo getUserById(int userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
    }

    public String updateUser(int userId, UserInfo userInfo) {
        return repository.findById(userId).map(existingUser -> {
            existingUser.setName(userInfo.getName());
            existingUser.setEmail(userInfo.getEmail());
            existingUser.setPassword(encoder.encode(userInfo.getPassword()));
            repository.save(existingUser);
            return "User updated successfully";
        }).orElse("User not found with ID: " + userId);
    }

    public String deleteUser(int userId) {
        return repository.findById(userId).map(user -> {
            repository.delete(user);
            return "User deleted successfully";
        }).orElse("User not found with ID: " + userId);
    }
}
