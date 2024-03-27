package com.javacore.task.controllers;

import com.javacore.task.models.TrainerModel;
import com.javacore.task.models.request.TrainerTrainingsRequest;
import com.javacore.task.models.request.TrainerUpdateRequest;
import com.javacore.task.models.response.TrainerInfoResponse;
import com.javacore.task.models.response.TrainerUpdateResponse;
import com.javacore.task.services.TrainerService;
import io.micrometer.core.instrument.Counter;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
class TrainerControllerTest {
    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    @Mock
    private Counter counter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void testGetTrainerById() {
        long trainerId = 2;
        TrainerModel expectedResponse = new TrainerModel();
        when(trainerService.getTrainerById(trainerId)).thenReturn(expectedResponse);

        ResponseEntity<TrainerModel> responseEntity = trainerController.getTrainerById(trainerId);

        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testUpdateTrainer() {
        TrainerUpdateRequest requestBody = new TrainerUpdateRequest("Kushtar.Amalbekov", "Alanush", "Mamanovichov", true);
        TrainerUpdateResponse expectedResponse = new TrainerUpdateResponse();
        when(trainerService.updateTrainer(any(TrainerUpdateRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<TrainerUpdateResponse> responseEntity = trainerController.updateTrainer(requestBody);

        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testGetTrainerProfile() {
        String username = "Aiperi.Adylova";
        TrainerInfoResponse expectedResponse = new TrainerInfoResponse();
        when(trainerService.findTrainerProfileByUsername(username)).thenReturn(expectedResponse);

        ResponseEntity<TrainerInfoResponse> responseEntity = trainerController.getTrainerProfileByName(username);

        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testUpdateTrainerStatus() {
        String username = "Aidana.Amalbekova";
        boolean choice = false;
        when(trainerService.updateTrainerStatus(choice, username)).thenReturn("Deactivated");

        ResponseEntity<String> responseEntity = trainerController.updateTrainerStatus(username, choice);

        assertEquals("Deactivated", responseEntity.getBody());
    }

    @Test
    void testGetTrainerTrainingsList() {
        TrainerTrainingsRequest requestBody = new TrainerTrainingsRequest(
                "Kushtar.Amalbekov",
                java.sql.Date.valueOf(LocalDate.of(2023, 10, 11)),
                java.sql.Date.valueOf(LocalDate.of(2024, 2, 11)),
                "Kanysh.Abdyrakmanova"
        );
        when(trainerService.getTrainerTrainingsByCriteria(any(TrainerTrainingsRequest.class))).thenReturn(Collections.emptyList());

        ResponseEntity<?> responseEntity = trainerController.getTrainerTrainingsList(requestBody);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}
