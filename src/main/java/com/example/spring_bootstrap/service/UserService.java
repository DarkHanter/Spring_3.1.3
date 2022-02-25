package com.example.spring_bootstrap.service;

import com.example.spring_bootstrap.model.User;

import java.util.List;

public interface UserService {
    void addUser(User user);
    void editUser(User user);
    void removeUser(long id);
    User getUser(long id);
    List<User> getAllUsers();
    boolean isEmailAvailable(String name);
    boolean isRoleAvailable(String name);
}