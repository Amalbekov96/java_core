package com.javacore.task.services;

public interface ProfileService {
    String generateUsername(String firstName, String lastName, String userType);

    String generateRandomPassword();
}
