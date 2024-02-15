package com.javacore.task.service;

import com.javacore.task.entities.TrainingType;
import com.javacore.task.enums.TrainingTypes;
import com.javacore.task.repositories.TrainingTypeRepository;
import com.javacore.task.services.impl.TrainingTypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceTest {
    @Mock
    private TrainingTypeRepository trainingTypeDao;
    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Test
    void testGetAllTrainingTypes() {

        List<TrainingType> expectedTrainingTypes = new ArrayList<>();
        expectedTrainingTypes.add(new TrainingType(1L, TrainingTypes.FITNESS));
        expectedTrainingTypes.add(new TrainingType(3L, TrainingTypes.WEIGHT_LIFTING));
        expectedTrainingTypes.add(new TrainingType(4L, TrainingTypes.YOGA));
        expectedTrainingTypes.add(new TrainingType(2L, TrainingTypes.CARDIO));


        when(trainingTypeDao.findAll()).thenReturn(expectedTrainingTypes);

        List<TrainingType> result = trainingTypeService.getAllTrainingTypes();

        verify(trainingTypeDao, times(1)).findAll();

        assertNotNull(result);
        assertEquals(expectedTrainingTypes, result);
        assertEquals(expectedTrainingTypes.size(), result.size());

    }
}
