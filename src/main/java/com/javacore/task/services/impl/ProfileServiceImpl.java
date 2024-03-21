package com.javacore.task.services.impl;

import com.javacore.task.repositories.UserRepository;
import com.javacore.task.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private static final Random RANDOM = new Random();
    public static final int PASSWORD_LENGTH = 10;
    @Override
    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;

        int serialNumber = 1;
        while (userRepository.existsByUsername(username)) {
            username = baseUsername + serialNumber;
            serialNumber++;
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
