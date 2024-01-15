package com.javacore.task;

import com.javacore.task.repositories.InMemoryStorage;
import com.javacore.task.services.impl.ProfileServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ProfileServiceImplTest {

    @Test
    public void testGenerateUsername() {
        // Mock data
        InMemoryStorage inMemoryStorage = Mockito.mock(InMemoryStorage.class);
        ProfileServiceImpl profileService = new ProfileServiceImpl(inMemoryStorage);

        String firstName = "John";
        String lastName = "Doe";
        String userType = "USER";

        // Mock usernameExists to return false initially and then true after one iteration
// Mock usernameExists to return false initially
        Mockito.when(inMemoryStorage.usernameExists(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        // Perform the test
        String result = profileService.generateUsername(firstName, lastName, userType);

        // Verify the interactions and result
        Mockito.verify(inMemoryStorage, Mockito.times(1)).usernameExists(Mockito.anyString(), Mockito.anyString());
        Assertions.assertEquals("John.Doe", result); // Assuming the initial username is "John.Doe"
    }

    @Test
    public void testGenerateRandomPassword() {
        // Mock data
        InMemoryStorage inMemoryStorage = Mockito.mock(InMemoryStorage.class);
        ProfileServiceImpl profileService = new ProfileServiceImpl(inMemoryStorage);

        // Perform the test
        String result = profileService.generateRandomPassword();

        // Verify the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(10, result.length());
    }
}
