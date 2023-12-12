package com.javacore.task;

import com.javacore.task.entities.User;
import com.javacore.task.enums.UserType;
import com.javacore.task.exceptions.StorageException;
import com.javacore.task.mappers.UserMapper;
import com.javacore.task.models.UserModel;
import com.javacore.task.repositories.UserRepository;
import com.javacore.task.services.impl.ProfileServiceImpl;
import com.javacore.task.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private ProfileServiceImpl profileService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Test
    public void testGetUserById_UserFound() {
        // Mock data
        Long userId = 1L;
        User userEntity = new User();
        userEntity.setUserId(userId);
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setUsername("john.doe");
        userEntity.setPassword("password123");
        userEntity.setIsActive(true);

        UserModel userModel = new UserModel();
        userModel.setUserId(userId);
        userModel.setFirstName("John");
        userModel.setLastName("Doe");
        userModel.setUsername("john.doe");
        userModel.setPassword("password123");
        userModel.setIsActive(true);

        // Mock userRepository.findById
        Mockito.when(userRepository.findById(userId)).thenReturn(userEntity);

        // Mock userMapper.userToUserModel
        Mockito.when(userMapper.userToUserModel(userEntity)).thenReturn(userModel);

        // Perform the tesJohnt
        UserModel result = userService.getUserById(userId);

        // Verify the result
        Assertions.assertEquals(userModel, result);
    }

    @Test
    public void testCreateUser_Success() throws StorageException {
        // Mock data
        UserModel userModel = new UserModel();
        userModel.setFirstName("John");
        userModel.setLastName("Doe");

        User userEntity = new User();
        userEntity.setUserId(1L);
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");

        // Mock userMapper.userModelToUser
        Mockito.when(userMapper.userModelToUser(userModel)).thenReturn(userEntity);

        // Mock profileService.generateUsername and generateRandomPassword
        Mockito.when(profileService.generateUsername("John", "Doe", UserType.USER.getDescription())).thenReturn("john.doe");
        Mockito.when(profileService.generateRandomPassword()).thenReturn("px`assword123");

        // Mock userRepository.save
        Mockito.when(userRepository.save(userEntity)).thenReturn(userEntity);

        // Perform the test
        UserModel result = userService.createUser(userModel);

        // Verify the state of the result
        Assertions.assertEquals("john.doe", result.getUsername());
        Assertions.assertEquals("password123", result.getPassword());
        Assertions.assertEquals("John", result.getFirstName());
        Assertions.assertEquals("Doe", result.getLastName());
        // Add more assertions if needed based on your UserModel structure
    }
}

