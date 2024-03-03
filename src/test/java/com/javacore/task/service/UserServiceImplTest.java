package com.javacore.task.service;

import com.javacore.task.entities.User;
import com.javacore.task.repositories.UserRepository;
import com.javacore.task.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testLoadUserByUsername_ExistingUser() {
        String username = "Amalbekov96";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String username = "nonexistent";
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.userDetailsService().loadUserByUsername(username));
    }
}