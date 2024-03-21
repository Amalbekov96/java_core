package com.javacore.task.controllers;

import com.javacore.task.entities.TrainingType;
import com.javacore.task.enums.TrainingTypes;
import com.javacore.task.services.TrainingTypeService;
import io.micrometer.core.instrument.Counter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TrainingTypeControllerTest {
    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private TrainingTypesRestController trainingTypeController;

    @Mock
    private Counter counter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetTrainingTypes() {
        List<TrainingType> trainingTypes = Arrays.asList(
                new TrainingType(1L, TrainingTypes.FITNESS),
                new TrainingType(2L, TrainingTypes.YOGA)
        );
        when(trainingTypeService.getAllTrainingTypes()).thenReturn(trainingTypes);
        ResponseEntity<List<TrainingType>> responseEntity = trainingTypeController.getTrainingTypes();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(trainingTypes, responseEntity.getBody());
    }
}
