package com.javacore.task.controllers;

import com.javacore.task.models.request.*;
import com.javacore.task.models.response.*;
import com.javacore.task.services.TraineeService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@Transactional
class TraineeControllerTest {
    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private TraineeController traineeController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testDeleteTrainee() {
        String username = "Kairat.Uzenov";
        doNothing().when(traineeService).deleteTrainee(username);

        ResponseEntity<String> response = traineeController.deleteTrainee(username);

        verify(traineeService).deleteTrainee(username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("deleted successfully", response.getBody());
    }


    @Test
    void testUpdateTrainee() {

        TraineeUpdateRequest requestBody =
                new TraineeUpdateRequest("Kanysh.Abdyrakmanova",
                        "Alanushka", "Mamanovichov",
                        new Date(),
                        "123 Main St",true);
        TraineeProfileUpdateResponse expectedResponse = new TraineeProfileUpdateResponse(/* Populate with expected response */);
        when(traineeService.updateTrainee(any(TraineeUpdateRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<TraineeProfileUpdateResponse> response = traineeController.updateTrainee(requestBody);

        verify(traineeService).updateTrainee(requestBody);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }


    @Test
    void testGetTraineeProfile() {

        String username = "Kanysh.Abdyrakmanova";
        TraineeInfoResponse expectedResponse = new TraineeInfoResponse();
        when(traineeService.findTraineeProfileByUsername(username)).thenReturn(expectedResponse);

        TraineeInfoResponse response = traineeController.getTraineeProfile(username).getBody();

        verify(traineeService).findTraineeProfileByUsername(username);
        assert response != null;
        assertEquals(expectedResponse.getFirstName(), response.getFirstName());
        assertEquals(expectedResponse.getLastName(), response.getLastName());
        assertEquals(expectedResponse.getAddress(), response.getAddress());
    }
    @Test
    void testUpdateTraineeStatus() {

        String username = "Kanysh.Abdyrakmanova";
        boolean choice = false;
        when(traineeService.updateTraineeStatus(choice, username)).thenReturn("Deactivated");

        ResponseEntity<String> response = traineeController.updateTraineeStatus(username, choice);

        verify(traineeService).updateTraineeStatus(choice, username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deactivated", response.getBody());
    }


    @Test
    void testGetNotAssignedTrainersForTrainee() {

        String username = "Kanysh.Abdyrakmanova";
        List<TrainersListResponse> expectedResponse = Arrays.asList(
                new TrainersListResponse("Kushtar.Amalbekov", "Kushtar", "Amalbekov", "FITNESS"),
                new TrainersListResponse("Aiperi.Adylova", "Aiperi", "Adylova", "FITNESS")
        );
        when(traineeService.getNotAssignedActiveTrainersListForTrainee(username)).thenReturn(expectedResponse);

        ResponseEntity<List<TrainersListResponse>> responseEntity = traineeController.getNotAssignedTrainersForTrainee(username);

        verify(traineeService).getNotAssignedActiveTrainersListForTrainee(username);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testUpdateTraineeTrainersList() {

        String username = "Kanysh.Abdyrakmanova";
        List<String> trainersUsernames = Arrays.asList("Kushtar.Amalbekov", "Aiperi.Adylova");
        List<TrainersListResponse> expectedResponse = Arrays.asList(new TrainersListResponse("Kushtar.Amalbekov", "Kushtar", "Amalbekov", "FITNESS"),
                new TrainersListResponse("Aiperi.Adylova", "Aiperi", "Adylova", "FITNESS"));
        ResponseEntity<List<TrainersListResponse>> expectedResponseEntity = ResponseEntity.ok(expectedResponse);

        when(traineeService.updateTraineeTrainersList(username, trainersUsernames)).thenReturn(expectedResponse);

        ResponseEntity<List<TrainersListResponse>> responseEntity = traineeController.updateTraineeTrainersList(username, trainersUsernames);


        verify(traineeService).updateTraineeTrainersList(username, trainersUsernames);
        assertEquals(expectedResponseEntity, responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testGetTraineeTrainingsList() {

        String username = "Kanysh.Abdyrakmanova";
        TraineeTrainingsRequest requestBody = new TraineeTrainingsRequest(
                username,
                "Kushtar.Amalbekov",
                "FITNESS",
                java.sql.Date.valueOf(LocalDate.of(2021, 10, 11)),
                java.sql.Date.valueOf(LocalDate.of(2024, 2, 11))
        );
        List<TraineeTrainingInfoResponse> expectedResponse = List.of(new TraineeTrainingInfoResponse());
        ResponseEntity<List<TraineeTrainingInfoResponse>> expectedResponseEntity = ResponseEntity.ok(expectedResponse);

        when(traineeService.getTraineeTrainingsByCriteria(requestBody)).thenReturn(expectedResponse);

        ResponseEntity<List<TraineeTrainingInfoResponse>> responseEntity = traineeController.getTraineeTrainingsList(requestBody);


        verify(traineeService).getTraineeTrainingsByCriteria(requestBody);
        assertEquals(expectedResponseEntity, responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

}
