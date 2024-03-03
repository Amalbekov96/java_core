package com.javacore.task.actuator;

import com.javacore.task.enums.UserRole;
import com.javacore.task.models.response.SignInResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HealthEndpointTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private HealthEndpoint healthEndpoint;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        healthEndpoint = new HealthEndpoint(restTemplate);
    }

    @Test
    void checkEndpointStatus_SignUpTrainee_Successful() {
        testEndpointSuccess(HealthEndpoint.EndpointType.SIGN_UP_TRAINEE);
    }

    @Test
    void checkEndpointStatus_SignUpTrainer_Successful() {
        testEndpointSuccess(HealthEndpoint.EndpointType.SIGN_UP_TRAINER);
    }

    @Test
    void checkEndpointStatus_SignIn_Successful() {
        testEndpointSuccess(HealthEndpoint.EndpointType.SIGN_IN);
    }

    @Test
    void checkEndpointStatus_Training_Successful() {
        testEndpointSuccess(HealthEndpoint.EndpointType.TRAINING);
    }

    @Test
    void checkEndpointStatus_UpdateTrainersList_Successful() {
        testEndpointSuccess(HealthEndpoint.EndpointType.UPDATE_TRAINERS_LIST);
    }

    @Test
    void checkEndpointStatus_TraineeTrainingsList_Successful() {
        testEndpointSuccess(HealthEndpoint.EndpointType.TRAINEE_TRAININGS_LIST);
    }

    @Test
    void checkEndpointStatus_TrainersTrainingsList_Successful() {
        testEndpointSuccess(HealthEndpoint.EndpointType.TRAINERS_TRAININGS_LIST);
    }

    @Test
    void checkEndpointStatus_ChangePassword_Successful() {
        testEndpointSuccess(HealthEndpoint.EndpointType.CHANGE_PASSWORD);
    }

    @Test
    void checkEndpointStatus_TrainersByName_Successful() {
        testEndpointSuccess(HealthEndpoint.EndpointType.TRAINERS_BY_NAME);
    }

    @Test
    void checkEndpointStatus_TraineesByName_Successful() {
        testEndpointSuccess(HealthEndpoint.EndpointType.TRAINEES_BY_NAME);
    }

    @Test
    void checkEndpointStatus_NotAssignedTrainers_Successful() {
        testEndpointSuccess(HealthEndpoint.EndpointType.NOT_ASSIGNED_TRAINERS);
    }

    @Test
    void checkEndpointStatus_DeleteTraineeById_Successful() {
        testEndpointSuccess(HealthEndpoint.EndpointType.DELETE_TRAINEE_BY_ID);
    }


    @Test
    void checkEndpointStatus_TrainerById_Successful() {
        testEndpointSuccess(HealthEndpoint.EndpointType.TRAINER_BY_ID);
    }

    @Test
    void checkEndpointStatus_UpdateTrainee_Successful() {
        testEndpointSuccess(HealthEndpoint.EndpointType.UPDATE_TRAINEE);
    }

    @Test
    void checkEndpointStatus_UpdateTrainer_Successful() {
        testEndpointSuccess(HealthEndpoint.EndpointType.UPDATE_TRAINER);
    }

    private void testEndpointSuccess(HealthEndpoint.EndpointType endpointType) {
        Mockito.when(restTemplate.exchange(
                        Mockito.eq(URI.create(HealthEndpoint.BASE_URL + HealthEndpoint.ENDPOINT_SIGN_IN)),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.any(HttpEntity.class),
                        Mockito.eq(SignInResponse.class)))
                .thenReturn(new ResponseEntity<>(new SignInResponse("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJLdXNodGFyLkFtYWxiZWtvdiIsImlhdCI6MTcwODc1MzAzNSwiZXhwIjoxNzA4NzU0NDc1fQ.kjwtnT9VtBzC9sOMKNJQ_9ZVyde2ZT0lpduPa8XZ81E","",UserRole.TRAINER), HttpStatus.OK));
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(healthEndpoint.getHttpMethod(endpointType)), Mockito.any(), Mockito.eq(String.class)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        String endpointUrl = healthEndpoint.getEndpointUrl(endpointType);

        Mockito.when(restTemplate.exchange(
                        Mockito.eq(URI.create(HealthEndpoint.BASE_URL + endpointUrl)),
                        Mockito.eq(healthEndpoint.getHttpMethod(endpointType)),
                        Mockito.any(HttpEntity.class),
                        Mockito.eq(String.class)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        boolean result = healthEndpoint.checkEndpointStatus(healthEndpoint.getEndpointUrl(endpointType), healthEndpoint.getHttpMethod(endpointType));

        assertTrue(result);
    }
}
