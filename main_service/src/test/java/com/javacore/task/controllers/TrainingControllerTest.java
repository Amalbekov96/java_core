package com.javacore.task.controllers;

import com.javacore.task.models.request.TrainingRequest;
import com.javacore.task.services.TrainingService;
import io.micrometer.core.instrument.Counter;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

@Transactional
class TrainingControllerTest {
    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    @Mock
    private Counter counter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateTraining() throws IOException {
        TrainingRequest requestBody = new TrainingRequest(
                "Kanysh.Abdyrakmanova",
                "Kushtar.Amalbekov",
                "FITNESS",
                new Date(),
                2
        );
        doNothing().when(trainingService).saveTraining(requestBody);
        ResponseEntity<String> responseEntity = trainingController.saveTraining(requestBody);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("saved successfully", responseEntity.getBody());
    }

}
