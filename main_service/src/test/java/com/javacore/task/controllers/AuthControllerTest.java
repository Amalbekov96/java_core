package com.javacore.task.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacore.task.entities.TrainingType;
import com.javacore.task.enums.TrainingTypes;
import com.javacore.task.enums.UserRole;
import com.javacore.task.exceptions.BadRequestException;
import com.javacore.task.models.request.PasswordUpdateRequest;
import com.javacore.task.models.request.SignInRequest;
import com.javacore.task.models.request.TraineeRequest;
import com.javacore.task.models.request.TrainerRequest;
import com.javacore.task.models.response.SignInResponse;
import com.javacore.task.models.response.SignUpResponse;
import com.javacore.task.services.AuthenticationService;
import com.javacore.task.services.BruteForceService;
import io.micrometer.core.instrument.Counter;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class AuthControllerTest {
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authController;
    private MockMvc mockMvc;
    @Mock
    private Counter counter;
    @Mock
    private BruteForceService bruteForceService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
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
        when(bruteForceService.isBlocked(signInRequest.getUsername()))
                .thenReturn(false);
        when(authenticationService.signIn(any(SignInRequest.class))).thenReturn(expectedResponse);

        SignInResponse responseEntity = authController.signIn(signInRequest);

        assertEquals(expectedResponse, responseEntity);
        verify(authenticationService, times(1)).signIn(signInRequest);


    }

    @Test
    void testSignIn_IncorrectRequest() {
        SignInRequest signInRequest = new SignInRequest("", "rwerw");
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        Validator validator = localValidatorFactoryBean;
        Set<ConstraintViolation<SignInRequest>> violations = validator.validate(signInRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testTraineeSignUp_InvalidField() {
        // Create a TraineeRequest with a blank first name
        TraineeRequest traineeRequest = new TraineeRequest("", "Doe", new Date(), "123 Main St");

        // Create a validator instance
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        Validator validator = localValidatorFactoryBean;

        // Validate the TraineeRequest using the validator
        Set<ConstraintViolation<TraineeRequest>> violations = validator.validate(traineeRequest);

        // Expect that there are validation violations for the blank first name
        assertFalse(violations.isEmpty());
    }

    @Test
    void testTrainerSignUp_InvalidField() {
        // Create a TrainerRequest with a null specialization
        TrainerRequest trainerRequest = new TrainerRequest("John", "Doe", null);

        // Create a validator instance
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        Validator validator = localValidatorFactoryBean;

        // Validate the TrainerRequest using the validator
        Set<ConstraintViolation<TrainerRequest>> violations = validator.validate(trainerRequest);

        // Expect that there are validation violations for the null specialization
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSignIn_InvalidField() {
        // Create a SignInRequest with a blank username
        SignInRequest signInRequest = new SignInRequest("", "ziJ4jlTA22");

        // Create a validator instance
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        Validator validator = localValidatorFactoryBean;

        // Validate the SignInRequest using the validator
        Set<ConstraintViolation<SignInRequest>> violations = validator.validate(signInRequest);

        // Expect that there are validation violations for the blank username
        assertFalse(violations.isEmpty());
    }

    @Test
    void testTraineeSignUp_InvalidField1() throws Exception {
        // Create a TraineeRequest with invalid data
        TraineeRequest traineeRequest = new TraineeRequest("", "Doe", new Date(), "123 Main St");

        // Perform the POST request and expect a 400 Bad Request response
        mockMvc.perform(post("/api/v1/auth/trainee/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(traineeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testTrainerSignUp_InvalidField1() throws Exception {
        // Create a TrainerRequest with invalid data
        TrainerRequest trainerRequest = new TrainerRequest("John", "Doe", null);

        // Perform the POST request and expect a 400 Bad Request response
        mockMvc.perform(post("/api/v1/auth/trainer/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(trainerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSignIn_InvalidField1() throws Exception {
        // Create a SignInRequest with invalid data
        SignInRequest signInRequest = new SignInRequest("", "ziJ4jlTA22");

        // Perform the POST request and expect a 400 Bad Request response
        mockMvc.perform(post("/api/v1/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signInRequest)))
                .andExpect(status().isBadRequest());
    }
}