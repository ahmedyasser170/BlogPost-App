package com.example.BlogPostApp.service;

import com.example.BlogPostApp.model.Role;
import com.example.BlogPostApp.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username,String roleName);
    User getUser(String userName);
    List<User> getUsers();
}
