package com.javacore.task.services;

import com.javacore.task.models.UserModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserModel getUserById(Long userId);

    UserModel createUser(UserModel userModel);

    UserModel updateUser(Long userId, UserModel userModel);

    void deleteUser(Long userId);

    UserDetailsService userDetailsService();
}
