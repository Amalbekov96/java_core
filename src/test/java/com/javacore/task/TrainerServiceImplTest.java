package com.javacore.task;

import com.javacore.task.entities.Trainer;
import com.javacore.task.exceptions.ApiException;
import com.javacore.task.exceptions.StorageException;
import com.javacore.task.mappers.TrainerMapper;
import com.javacore.task.models.TrainerModel;
import com.javacore.task.repositories.TrainerRepository;
import com.javacore.task.services.impl.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainerMapper trainerMapper;

    @Test
    void getTrainerById_withValidId_shouldReturnTrainerModel() {
        // Arrange
        Long trainerId = 1L;
        Trainer trainer = new Trainer();
        trainer.setId(trainerId);
        TrainerModel trainerModel = new TrainerModel();
        trainerModel.setId(trainerId);

        when(trainerRepository.findById(trainerId)).thenReturn(trainer);
        when(trainerMapper.trainerToTrainerModel(trainer)).thenReturn(trainerModel);

        // Act
        TrainerModel result = trainerService.getTrainerById(trainerId);

        // Assert
        assertEquals(trainerModel, result);
    }

    @Test
    void getTrainerById_withInvalidId_shouldThrowApiException() {
        // Arrange
        Long trainerId = 1L;

        when(trainerRepository.findById(trainerId)).thenReturn(null);

        // Act and Assert
        assertThrows(ApiException.class, () -> trainerService.getTrainerById(trainerId));
    }

    @Test
    void createTrainer_withValidData_shouldReturnTrainerModel() throws StorageException {
        // Arrange
        TrainerModel trainerModel = new TrainerModel();
        Trainer trainerEntity = new Trainer();

        when(trainerMapper.trainerModelToTrainer(trainerModel)).thenReturn(trainerEntity);
        when(trainerRepository.save(trainerEntity)).thenReturn(trainerEntity);
        when(trainerMapper.trainerToTrainerModel(trainerEntity)).thenReturn(trainerModel);

        // Act
        TrainerModel result = trainerService.createTrainer(trainerModel);

        // Assert
        assertEquals(trainerModel, result);
    }

    @Test
    void updateTrainer_withValidData_shouldReturnTrainerModel() throws StorageException {
        // Arrange
        Long trainerId = 1L;
        TrainerModel trainerModel = new TrainerModel();
        Trainer existingTrainer = new Trainer();
        existingTrainer.setId(trainerId);

        when(trainerRepository.findById(trainerId)).thenReturn(existingTrainer);
        when(trainerRepository.save(existingTrainer)).thenReturn(existingTrainer);
        when(trainerMapper.trainerToTrainerModel(existingTrainer)).thenReturn(trainerModel);

        // Act
        TrainerModel result = trainerService.updateTrainer(trainerId, trainerModel);

        // Assert
        assertEquals(trainerModel, result);
    }

    @Test
    void updateTrainer_withInvalidId_shouldThrowApiException() {
        // Arrange
        Long trainerId = 1L;
        TrainerModel trainerModel = new TrainerModel();

        when(trainerRepository.findById(trainerId)).thenReturn(null);

        // Act and Assert
        assertThrows(ApiException.class, () -> trainerService.updateTrainer(trainerId, trainerModel));
    }

    @Test
    void deleteTrainer_withValidId_shouldNotThrowException() {
        // Arrange
        Long trainerId = 1L;

        // Act and Assert
        assertDoesNotThrow(() -> trainerService.deleteTrainer(trainerId));
    }

    @Test
    void deleteTrainer_withInvalidId_shouldThrowApiException() {
        // Arrange
        Long trainerId = 1L;

        doThrow(new EmptyResultDataAccessException(1)).when(trainerRepository).deleteById(trainerId);

        // Act and Assert
        assertThrows(ApiException.class, () -> trainerService.deleteTrainer(trainerId));
    }
}
