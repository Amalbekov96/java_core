package com.javacore.task.services.impl;

import com.javacore.task.entities.User;
import com.javacore.task.enums.ErrorCode;
import com.javacore.task.enums.UserType;
import com.javacore.task.exceptions.ApiException;
import com.javacore.task.exceptions.StorageException;
import com.javacore.task.mappers.UserMapper;
import com.javacore.task.models.UserModel;
import com.javacore.task.repositories.UserRepository;
import com.javacore.task.services.ProfileService;
import com.javacore.task.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProfileService profileService;

    @Override
    public UserModel getUserById(Long userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            return userMapper.userToUserModel(user);
        } else {
            throw new ApiException("User with ID " + userId + " not found", ErrorCode.USER_NOT_FOUND);
        }
    }

    @Override
    public UserModel createUser(UserModel userModel) {
        try {
            User userEntity = userMapper.userModelToUser(userModel);
            String username = profileService.generateUsername(userEntity.getFirstName(), userEntity.getLastName(), UserType.USER.getDescription());
            String password = profileService.generateRandomPassword();
            userEntity.setUsername(username);
            userEntity.setPassword(password);
            userEntity = userRepository.save(userEntity);
            return userMapper.userToUserModel(userEntity);
        } catch (Exception | StorageException e) {
            log.error("Error creating User", e);
            throw new ApiException("Error creating User", ErrorCode.USER_NOT_FOUND);
        }
    }

    @Override
    public UserModel updateUser(Long userId, UserModel userModel) {
        try {
            User existingUser = userRepository.findById(userId);
            if (existingUser == null) {
                throw new ApiException("User with ID " + userId + " not found", ErrorCode.USER_NOT_FOUND);
            }

            userMapper.update(userModel, existingUser);
            existingUser = userRepository.save(existingUser);

            return userMapper.userToUserModel(existingUser);
        } catch (Exception | StorageException e) {
            log.error("Error updating User", e);
            throw new ApiException("Error updating User", ErrorCode.USER_NOT_FOUND);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (Exception e) {
            log.error("Error deleting User", e);
            throw new ApiException("Error deleting User", ErrorCode.USER_NOT_FOUND);
        }
    }
}
