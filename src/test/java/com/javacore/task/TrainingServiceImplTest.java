package com.javacore.task;

import com.javacore.task.entities.Training;
import com.javacore.task.exceptions.ApiException;
import com.javacore.task.exceptions.StorageException;
import com.javacore.task.mappers.TrainingMapper;
import com.javacore.task.models.TraineeModel;
import com.javacore.task.models.TrainerModel;
import com.javacore.task.models.TrainingModel;
import com.javacore.task.repositories.TrainingRepository;
import com.javacore.task.services.TraineeService;
import com.javacore.task.services.TrainerService;
import com.javacore.task.services.impl.TrainingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainingMapper trainingMapper;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Test
    void getTrainingById_withValidId_shouldReturnTrainingModel() {
        // Arrange
        Long trainingId = 1L;
        Training training = new Training();
        training.setId(trainingId);
        TrainingModel trainingModel = new TrainingModel();
        trainingModel.setId(trainingId);

        when(trainingRepository.findById(trainingId)).thenReturn(training);
        when(trainingMapper.trainingToTrainingModel(training)).thenReturn(trainingModel);

        // Act
        TrainingModel result = trainingService.getTrainingById(trainingId);

        // Assert
        assertEquals(trainingModel, result);
    }

    @Test
    void getTrainingById_withInvalidId_shouldThrowApiException() {
        // Arrange
        Long trainingId = 1L;

        when(trainingRepository.findById(trainingId)).thenReturn(null);

        // Act and Assert
        assertThrows(ApiException.class, () -> trainingService.getTrainingById(trainingId));
    }

    @Test
    void createTraining_withValidData_shouldReturnTrainingModel() throws StorageException {
        // Arrange
        // Create real TraineeModel
        TraineeModel traineeModel = new TraineeModel();
        traineeModel.setTraineeId(1L); // Set appropriate values

        // Create real TrainerModel
        TrainerModel trainerModel = new TrainerModel();
        trainerModel.setId(1L); // Set appropriate values

        TrainingModel trainingModel = new TrainingModel();
        trainingModel.setTrainee(traineeModel);
        trainingModel.setTrainer(trainerModel);
        Training trainingEntity = new Training();

        // Set up stubs
        when(trainerService.getTrainerById(eq(trainerModel.getId())))
                .thenReturn(trainerModel);

        when(traineeService.getTraineeById(eq(traineeModel.getTraineeId())))
                .thenReturn(traineeModel);

        when(trainingMapper.trainingModelToTraining(trainingModel)).thenReturn(trainingEntity);
        when(trainingRepository.save(trainingEntity)).thenReturn(trainingEntity);
        when(trainingMapper.trainingToTrainingModel(trainingEntity)).thenReturn(trainingModel);

        // Act
        TrainingModel result = trainingService.createTraining(trainingModel);

        // Assert
        assertEquals(trainingModel, result);
    }


    @Test
    void createTraining_withMissingTrainer_shouldThrowApiException() {
        // Arrange
        TrainingModel trainingModel = new TrainingModel();
        trainingModel.setTrainee(new TraineeModel());

        // Act and Assert
        assertThrows(ApiException.class, () -> trainingService.createTraining(trainingModel));
    }

    @Test
    void createTraining_withMissingTrainee_shouldThrowApiException() {
        // Arrange
        TrainingModel trainingModel = new TrainingModel();
        trainingModel.setTrainer(new TrainerModel());

        // Act and Assert
        assertThrows(ApiException.class, () -> trainingService.createTraining(trainingModel));
    }

    @Test
    void updateTraining_withValidData_shouldReturnTrainingModel() throws StorageException {
        // Arrange
        Long trainingId = 1L;
        TrainingModel trainingModel = new TrainingModel();
        Training existingTraining = new Training();
        existingTraining.setId(trainingId);

        when(trainingRepository.findById(trainingId)).thenReturn(existingTraining);
        when(trainingRepository.save(existingTraining)).thenReturn(existingTraining);
        when(trainingMapper.trainingToTrainingModel(existingTraining)).thenReturn(trainingModel);

        // Act
        TrainingModel result = trainingService.updateTraining(trainingId, trainingModel);

        // Assert
        assertEquals(trainingModel, result);
    }

    @Test
    void updateTraining_withInvalidId_shouldThrowApiException() {
        // Arrange
        Long trainingId = 1L;
        TrainingModel trainingModel = new TrainingModel();

        when(trainingRepository.findById(trainingId)).thenReturn(null);

        // Act and Assert
        assertThrows(ApiException.class, () -> trainingService.updateTraining(trainingId, trainingModel));
    }

    @Test
    void deleteTraining_withValidId_shouldNotThrowException() {
        // Arrange
        Long trainingId = 1L;

        // Act and Assert
        assertDoesNotThrow(() -> trainingService.deleteTraining(trainingId));
    }

    @Test
    void deleteTraining_withInvalidId_shouldThrowApiException() {
        // Arrange
        Long trainingId = 1L;

        doThrow(new EmptyResultDataAccessException(1)).when(trainingRepository).deleteById(trainingId);

        // Act and Assert
        assertThrows(ApiException.class, () -> trainingService.deleteTraining(trainingId));
    }
}
