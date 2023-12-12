package com.javacore.task;

import com.javacore.task.entities.User;
import com.javacore.task.exceptions.ApiException;
import com.javacore.task.exceptions.StorageException;
import com.javacore.task.mappers.UserMapper;
import com.javacore.task.models.UserModel;
import com.javacore.task.repositories.UserRepository;
import com.javacore.task.services.ProfileService;
import com.javacore.task.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileService profileService;

    @Mock
    private UserMapper userMapper;
    @Test
    void getUserById_withValidId_shouldReturnUserModel() {
        // Arrange
        Long userId = 1L;
        User userEntity = new User();
        userEntity.setUserId(userId);
        UserModel userModel = new UserModel();
        userModel.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(userEntity);
        when(userMapper.userToUserModel(userEntity)).thenReturn(userModel);

        // Act
        UserModel result = userService.getUserById(userId);

        // Assert
        assertEquals(userModel, result);
    }

    @Test
    void getUserById_withInvalidId_shouldThrowApiException() {
        // Arrange
        Long userId = 1L;

        // Make the stubbing lenient
        lenient().when(userRepository.findById(userId)).thenReturn(null);
        // Act and Assert
        assertThrows(ApiException.class, () -> userService.getUserById(userId));
    }

    @Test
    void createUser_withValidData_shouldReturnUserModel() throws StorageException {
        // Arrange
        UserModel userModel = new UserModel();
        User userEntity = new User();

        when(userMapper.userModelToUser(userModel)).thenReturn(userEntity);
        when(profileService.generateUsername(any(), any(), any())).thenReturn("username");
        when(profileService.generateRandomPassword()).thenReturn("password");
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.userToUserModel(userEntity)).thenReturn(userModel);

        // Act
        UserModel result = userService.createUser(userModel);

        // Assert
        assertEquals(userModel, result);
    }

    @Test
    void createUser_withException_shouldThrowApiException() throws StorageException {
        // Arrange
        UserModel userModel = new UserModel();

        when(userMapper.userModelToUser(userModel)).thenReturn(new User());
        when(profileService.generateUsername(any(), any(), any())).thenReturn("username");
        when(profileService.generateRandomPassword()).thenReturn("password");
        when(userRepository.save(any())).thenThrow(new StorageException("Storage error"));

        // Act and Assert
        assertThrows(ApiException.class, () -> userService.createUser(userModel));
    }

    @Test
    void updateUser_withValidData_shouldReturnUserModel() throws StorageException {
        // Arrange
        Long userId = 1L;
        UserModel userModel = new UserModel();
        User existingUser = new User();
        existingUser.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.userToUserModel(existingUser)).thenReturn(userModel);

        // Act
        UserModel result = userService.updateUser(userId, userModel);

        // Assert
        assertEquals(userModel, result);
    }

    @Test
    void updateUser_withInvalidId_shouldThrowApiException() {
        // Arrange
        Long userId = 1L;
        UserModel userModel = new UserModel();

        when(userRepository.findById(userId)).thenReturn(null);

        // Act and Assert
        assertThrows(ApiException.class, () -> userService.updateUser(userId, userModel));
    }

    @Test
    void deleteUser_withValidId_shouldNotThrowException() {
        // Arrange
        Long userId = 1L;

        // Act and Assert
        assertDoesNotThrow(() -> userService.deleteUser(userId));
    }

    @Test
    void deleteUser_withInvalidId_shouldThrowApiException() {
        // Arrange
        Long userId = 1L;

        doThrow(new EmptyResultDataAccessException(1)).when(userRepository).deleteById(userId);

        // Act and Assert
        assertThrows(ApiException.class, () -> userService.deleteUser(userId));
    }
}