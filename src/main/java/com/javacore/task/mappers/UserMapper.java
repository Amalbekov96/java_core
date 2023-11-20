package com.javacore.task.mappers;

import com.javacore.task.entities.User;
import com.javacore.task.models.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserModel userToUserModel(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserId(user.getUserId());
        userModel.setFirstName(user.getFirstName());
        userModel.setLastName(user.getLastName());
        userModel.setUsername(user.getUsername());
        userModel.setPassword(user.getPassword());
        userModel.setIsActive(user.getIsActive());
        return userModel;
    }

    public User userModelToUser(UserModel userModel) {
        User user = new User();
        user.setUserId(userModel.getUserId());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setUsername(userModel.getUsername());
        user.setPassword(userModel.getPassword());
        user.setIsActive(userModel.getIsActive());
        return user;
    }

    public void update(UserModel userModel, User user) {
        if (userModel != null && user != null) {
            user.setUserId(userModel.getUserId());
            user.setFirstName(userModel.getFirstName());
            user.setLastName(userModel.getLastName());
            user.setUsername(userModel.getUsername());
            user.setPassword(userModel.getPassword());
            user.setIsActive(userModel.getIsActive());
            // Update other fields as needed
        }
    }
}
