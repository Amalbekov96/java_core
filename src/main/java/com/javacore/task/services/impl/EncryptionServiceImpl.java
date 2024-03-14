package com.javacore.task.services.impl;

import com.javacore.task.services.EncryptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.rmi.UnexpectedException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Slf4j
@Service
public class EncryptionServiceImpl implements EncryptionService {

    private static final int SALT_LENGTH = 16;

    @Override
    public String encode(CharSequence rawPassword) {
        String salt = generateSalt();

        try {
            return hashPassword(rawPassword.toString(), salt);
        } catch (UnexpectedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        if (encodedPassword == null || encodedPassword.length() == 0) {
            log.warn("Empty encoded password");
            return false;
        }
        try {
            return checkPassword(rawPassword.toString(), encodedPassword);
        } catch (UnexpectedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkPassword(String password, String encodedPassword) throws UnexpectedException {
        int dotIndex = encodedPassword.indexOf('.');
        String storedSalt = encodedPassword.substring(0, dotIndex);

        // Concatenate the provided raw password with the stored salt
        return isEqualPasswords(encodedPassword, hashPassword(password, storedSalt));
    }

    private String hashPassword(String password, String salt) throws UnexpectedException {
        try {
            // Concatenate the password and salt
            String passwordWithSalt = password + salt;

            // Use SHA-256 for hashing
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(passwordWithSalt.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal representation
            StringBuilder hexString = new StringBuilder();

            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);

                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }

            // Concatenate the salt with the hashed password as a string
            return salt + "." + hexString;
        } catch (NoSuchAlgorithmException e) {
            throw new UnexpectedException("Error encoding password, e " + e.getMessage());
        }
    }

    private boolean isEqualPasswords(String a, String b) {
        return MessageDigest.isEqual(a.getBytes(StandardCharsets.UTF_8), b.getBytes(StandardCharsets.UTF_8));
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[SALT_LENGTH];
        random.nextBytes(saltBytes);

        // Convert the salt byte array to a hexadecimal representation
        StringBuilder hexString = new StringBuilder();
        for (byte b : saltBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
