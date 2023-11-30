package com.javacore.task.services.impl;

import com.javacore.task.entities.User;
import com.javacore.task.enums.ErrorCode;
import com.javacore.task.enums.UserRole;
import com.javacore.task.exceptions.ApiException;
import com.javacore.task.exceptions.StorageException;
import com.javacore.task.exceptions.UserNotFoundException;
import com.javacore.task.mappers.UserMapper;
import com.javacore.task.models.UserModel;
import com.javacore.task.repositories.UserRepository;
import com.javacore.task.services.ProfileService;
import com.javacore.task.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", userId)));
        return userMapper.userToUserModel(user);
    }

    @Override
    public UserModel createUser(UserModel userModel) {
        try {
            User userEntity = userMapper.userModelToUser(userModel);
            String username = profileService.generateUsername(userEntity.getFirstName(), userEntity.getLastName(), UserRole.USER.getDescription());
            String password = profileService.generateRandomPassword();
            userEntity.setUsername(username);
            userEntity.setPassword(password);
            userEntity = userRepository.save(userEntity);
            return userMapper.userToUserModel(userEntity);
        } catch (Exception e) {
            log.error("Error creating User", e);
            throw new ApiException("Error creating User", ErrorCode.USER_NOT_FOUND);
        }
    }

    @Override
    public UserModel updateUser(Long userId, UserModel userModel) {
        try {
            User existingUser = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", userId)));

            userMapper.update(userModel, existingUser);
            existingUser = userRepository.save(existingUser);

            return userMapper.userToUserModel(existingUser);
        } catch (Exception e) {
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

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }
}
