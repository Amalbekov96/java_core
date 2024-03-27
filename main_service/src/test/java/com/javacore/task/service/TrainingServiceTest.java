package com.javacore.task.service;

import com.javacore.task.entities.Trainee;
import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.Training;
import com.javacore.task.exceptions.UserNotFoundException;
import com.javacore.task.models.request.TrainingRequest;
import com.javacore.task.repositories.TraineeRepository;
import com.javacore.task.repositories.TrainerRepository;
import com.javacore.task.repositories.TrainingRepository;
import com.javacore.task.services.impl.TrainingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingDao;

    @Mock
    private TraineeRepository traineeDao;

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    void testAddTraining() {
        TrainingRequest trainingRequest = new TrainingRequest(
                "Kanysh.Abdyrakmanova1",
                "Aiperi.Adylova",
                "Weight_Lifting",
                new Date(),
                60
        );
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        when(traineeDao.findTraineeByUserUsername("Kanysh.Abdyrakmanova1")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUserUsername("Aiperi.Adylova")).thenReturn(Optional.of(trainer));

        trainingService.saveTraining(trainingRequest);

        verify(traineeDao, times(1)).findTraineeByUserUsername("Kanysh.Abdyrakmanova1");
        verify(trainerRepository, times(1)).findByUserUsername("Aiperi.Adylova");
        verify(trainingDao, times(1)).save(any(Training.class));

    }

    @Test
    void testSaveTraining_TraineeNotFound() {
        TrainingRequest trainingRequest = new TrainingRequest(
                "NonexistentTrainee",
                "Aiperi.Adylova",
                "Weight_Lifting",
                new Date(),
                60
        );
        when(traineeDao.findTraineeByUserUsername("NonexistentTrainee")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainingService.saveTraining(trainingRequest));
    }

    @Test
    void testSaveTraining_TrainerNotFound() {
        TrainingRequest trainingRequest = new TrainingRequest(
                "Kanysh.Abdyrakmanova1",
                "NonexistentTrainer",
                "Weight_Lifting",
                new Date(),
                60
        );
        Trainee trainee = new Trainee();
        when(traineeDao.findTraineeByUserUsername("Kanysh.Abdyrakmanova1")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUserUsername("NonexistentTrainer")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainingService.saveTraining(trainingRequest));
    }

}
