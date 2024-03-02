package com.javacore.task.service;

import com.javacore.task.repositories.UserRepository;
import com.javacore.task.services.impl.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfileServiceImplTest {

    private UserRepository userRepository;
    private ProfileServiceImpl profileService;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        profileService = new ProfileServiceImpl(userRepository);
    }

    @Test
    public void testGenerateUsername() {
        String firstName = "John";
        String lastName = "Doe";
        String baseUsername = firstName + "." + lastName;

        when(userRepository.existsByUsername(baseUsername)).thenReturn(true);
        when(userRepository.existsByUsername(baseUsername + "1")).thenReturn(false);

        String username = profileService.generateUsername(firstName, lastName);

        assertEquals(baseUsername + "1", username);
    }

    @Test
    public void testGenerateRandomPassword() {
        String password = profileService.generateRandomPassword();

        assertEquals(ProfileServiceImpl.PASSWORD_LENGTH, password.length());
    }
}