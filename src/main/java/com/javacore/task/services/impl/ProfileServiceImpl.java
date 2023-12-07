package com.javacore.task.services.impl;

import com.javacore.task.configs.InMemoryStorage;
import com.javacore.task.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private final InMemoryStorage inMemoryStorage;
    private static final Random RANDOM = new Random();
    private static final int PASSWORD_LENGTH = 10;
    @Override
    public String generateUsername(String firstName, String lastName, String userType) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;

        // Check if the username already exists
        if(inMemoryStorage != null) {
            int serialNumber = 1;
            while (inMemoryStorage.usernameExists(username, userType)) {
                username = baseUsername + serialNumber;
                serialNumber++;
            }
        }

        return username;
    }

    @Override
    public String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = RANDOM.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }

}
