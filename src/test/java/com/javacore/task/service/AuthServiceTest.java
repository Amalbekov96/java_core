package com.javacore.task.service;

import com.javacore.task.entities.Trainee;
import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.TrainingType;
import com.javacore.task.entities.User;
import com.javacore.task.enums.UserRole;
import com.javacore.task.models.request.SignInRequest;
import com.javacore.task.models.request.TraineeRequest;
import com.javacore.task.models.request.TrainerRequest;
import com.javacore.task.models.response.SignInResponse;
import com.javacore.task.models.response.SignUpResponse;
import com.javacore.task.repositories.TraineeRepository;
import com.javacore.task.repositories.TrainerRepository;
import com.javacore.task.repositories.TrainingTypeRepository;
import com.javacore.task.repositories.UserRepository;
import com.javacore.task.services.JwtService;
import com.javacore.task.services.impl.AuthenticationServiceImpl;
import com.javacore.task.services.impl.ProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private ProfileServiceImpl profileService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void testTraineeSignUp() {
        TraineeRequest request = new TraineeRequest(
                "John",
                "Doe",
                new Date(),"123 Main St"
        );
        User user = new User();
        Trainee trainee = new Trainee();
        trainee.setTraineeId(3L);
        trainee.setUser(user);
        when(profileService.generateUsername(any(), any())).thenReturn("username");
        when(profileService.generateRandomPassword()).thenReturn("password");
        when(userRepository.save(any())).thenReturn(user);
        when(traineeRepository.save(any())).thenReturn(trainee);
        when(jwtService.generateToken(any())).thenReturn("token");

        SignUpResponse response = authenticationService.traineeSignUp(request);

        assertNotNull(response);
        assertEquals("username", response.username());
        assertEquals("password", response.generatedPassword());
        assertEquals(UserRole.TRAINEE, response.role());
        verify(userRepository, times(1)).save(any());
        verify(traineeRepository, times(1)).save(any());
    }

    @Test
    void testTrainerSignUp() {

        TrainerRequest request = new TrainerRequest(
                "John",
                "Doe",
                new TrainingType()
        );
        User user = new User();
        Trainer trainer = new Trainer();
        trainer.setId(3L);
        when(profileService.generateUsername(any(), any())).thenReturn("username");
        when(profileService.generateRandomPassword()).thenReturn("password");
        when(userRepository.save(any())).thenReturn(user);
        when(trainingTypeRepository.findById(any())).thenReturn(Optional.of(new TrainingType())); // Assuming constructor parameters
        when(trainerRepository.save(any())).thenReturn(trainer);
        when(jwtService.generateToken(any())).thenReturn("token");


        SignUpResponse response = authenticationService.trainerSignUp(request);

        assertNotNull(response);
        assertEquals("username", response.username());
        assertEquals("password", response.generatedPassword());
        assertEquals(UserRole.TRAINER, response.role());
        verify(userRepository, times(1)).save(any());
        verify(trainingTypeRepository, times(1)).findById(any());
        verify(trainerRepository, times(1)).save(any());
    }
    @Test
    void testSignIn() {

        SignInRequest request = new SignInRequest(
                "username",
                "password"
        );
        User user = new User();
        user.setRole(UserRole.TRAINEE);
        user.setUsername("username");
        when(userRepository.findUserByUsername(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtService.generateToken(any())).thenReturn("token");


        SignInResponse response = authenticationService.signIn(request);


        assertNotNull(response);
        assertEquals("token", response.token());
        assertEquals(user.getUsername(), response.username());
        assertEquals(user.getRole(), response.role());
    }
}
