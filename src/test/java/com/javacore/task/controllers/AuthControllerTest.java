package com.javacore.task.controllers;

import com.javacore.task.entities.TrainingType;
import com.javacore.task.enums.TrainingTypes;
import com.javacore.task.enums.UserRole;
import com.javacore.task.models.request.SignInRequest;
import com.javacore.task.models.request.TraineeRequest;
import com.javacore.task.models.request.TrainerRequest;
import com.javacore.task.models.response.SignInResponse;
import com.javacore.task.models.response.SignUpResponse;
import com.javacore.task.services.AuthenticationService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Date;

@Transactional
class AuthControllerTest {
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testTraineeSignUp() {

        TraineeRequest traineeRequest = new TraineeRequest("John", "Doe", new Date(), "123 Main St");
        SignUpResponse expectedResponse = new SignUpResponse(
                "token",
                "John.Doe",
                "rfgtyuiop",
                UserRole.TRAINEE
        );
        when(authenticationService.traineeSignUp(any(TraineeRequest.class))).thenReturn(expectedResponse);

        SignUpResponse responseEntity = authController.traineeSignUp(traineeRequest);

        assertEquals(expectedResponse, responseEntity);
        verify(authenticationService, times(1)).traineeSignUp(traineeRequest);

    }




    @Test
    void testTrainerSignUp() {

        TrainerRequest trainerRequest = new TrainerRequest("John", "Doe", new TrainingType(3L,TrainingTypes.WEIGHT_LIFTING));
        SignUpResponse expectedResponse = new SignUpResponse(
                "token",
                "John.Doe",
                "rfgtyuiop",
                UserRole.TRAINER
        );
        when(authenticationService.trainerSignUp(any(TrainerRequest.class))).thenReturn(expectedResponse);

        SignUpResponse responseEntity = authController.trainerSignUp(trainerRequest);

        assertEquals(expectedResponse, responseEntity);
        verify(authenticationService, times(1)).trainerSignUp(trainerRequest);

    }

    @Test
    void testSignIn() {

        SignInRequest signInRequest = new SignInRequest("Kushtar.Amalbekov", "ziJ4jlTA22");
        SignInResponse expectedResponse = new SignInResponse(
                "token",
                "Kushtar.Amalbekov",
                UserRole.TRAINER
        );
        when(authenticationService.signIn(any(SignInRequest.class))).thenReturn(expectedResponse);

        SignInResponse responseEntity = authController.signIn(signInRequest);

        assertEquals(expectedResponse, responseEntity);
        verify(authenticationService, times(1)).signIn(signInRequest);


    }
}