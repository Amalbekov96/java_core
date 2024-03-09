package com.javacore.task.service;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.Training;
import com.javacore.task.entities.User;
import com.javacore.task.exceptions.UserNotFoundException;
import com.javacore.task.mappers.TrainerMapper;
import com.javacore.task.mappers.TrainingMapper;
import com.javacore.task.models.request.TrainerTrainingsRequest;
import com.javacore.task.models.request.TrainerUpdateRequest;
import com.javacore.task.models.response.TrainerTrainingInfoResponse;
import com.javacore.task.repositories.TraineeRepository;
import com.javacore.task.repositories.TrainerRepository;
import com.javacore.task.services.impl.TrainerServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
 class TrainerServiceTest {
    @Mock
    private TrainerRepository trainerRepository;
    @InjectMocks
    private TrainerServiceImpl trainerService;
    @Mock
    private TrainingMapper trainingMapper;

    @Mock
    private TrainerMapper trainerMapper;
    @Test
    @Transactional
    void testUpdateTrainer() {

        TrainerUpdateRequest trainerUpdateRequest = new TrainerUpdateRequest(
                "Kushtar.Amalbekov",
                "John",
                "Yoki",
                true
        );

        Trainer existingTrainer = new Trainer();
        User user = new User();
        existingTrainer.setUser(user);

        when(trainerRepository.findByUserUsername("Kushtar.Amalbekov")).thenReturn(Optional.of(existingTrainer));
        trainerService.updateTrainer(trainerUpdateRequest);

        verify(trainerRepository, times(1)).findByUserUsername("Kushtar.Amalbekov");
        verify(trainerRepository, times(1)).save(any());
    }

    @Test
    void testSelectTrainerProfileByUsername() {
        Trainer trainer = new Trainer();
        trainer.setUser(new User());
        trainer.getUser().setUsername("Aiperi.Adylova");
        when(trainerRepository.findByUserUsername("Aiperi.Adylova")).thenReturn(Optional.of(trainer));
        trainerService.findTrainerProfileByUsername("Aiperi.Adylova");
        verify(trainerRepository, times(1)).findByUserUsername("Aiperi.Adylova");
    }
    @Test
    void testGetTrainerTrainingsByCriteria() {

        Date periodFrom = java.sql.Date.valueOf(LocalDate.now().minusDays(7));
        Date periodTo = java.sql.Date.valueOf(LocalDate.now());

        TrainerTrainingsRequest request = new TrainerTrainingsRequest(
                "Eulan.Ibraimov",
                periodFrom,
                periodTo,
                "Kanysh.Abdyrakmanova"
        );

        List<Training> trainings = Collections.singletonList(new Training());

        when(trainerRepository.getTrainerTrainingsByCriteria(
                eq(request.getUsername()),
                eq(request.getPeriodFrom()),
                eq(request.getPeriodTo()),
                eq(request.getTraineeName())
        )).thenReturn(Optional.of(trainings));
        when(trainerRepository.existsByUserUsername(request.getUsername())).thenReturn(true);

        List<TrainerTrainingInfoResponse> result = trainerService.getTrainerTrainingsByCriteria(request);

        verify(trainerRepository, times(1)).getTrainerTrainingsByCriteria(
                eq(request.getUsername()),
                eq(request.getPeriodFrom()),
                eq(request.getPeriodTo()),
                eq(request.getTraineeName())
        );

        assertEquals(0, result.size());
    }



    @Test
    void testGetTrainerById_TrainerNotFound() {
        Long trainerId = 1L;
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainerService.getTrainerById(trainerId));
    }

    @Test
    void testUpdateTrainer_TrainerNotFound() {
        TrainerUpdateRequest request = new TrainerUpdateRequest(
                "Kushtar.Amalbekov",
                "John",
                "Yoki",
                true
        );
        when(trainerRepository.findByUserUsername(request.getUserName())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainerService.updateTrainer(request));
    }

    @Test
    void testFindTrainerProfileByUsername_TrainerNotFound() {
        String username = "Kushtar.Amalbekov";
        when(trainerRepository.findByUserUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainerService.findTrainerProfileByUsername(username));
    }

    @Test
    void testGetTrainerTrainingsByCriteria_TrainerNotFound() {
        TrainerTrainingsRequest request = new TrainerTrainingsRequest(
                "Eulan.Ibraimov",
                null,
                null,
                "Kanysh.Abdyrakmanova"
        );
        when(trainerRepository.existsByUserUsername(request.getUsername())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> trainerService.getTrainerTrainingsByCriteria(request));
    }



    @Test
    void testUpdateTrainerStatus_TrainerNotFound() {
        String username = "Kushtar.Amalbekov";
        boolean status = true;
        when(trainerRepository.findByUserUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainerService.updateTrainerStatus(status, username));
    }

    @Test
    void testUpdateTrainerStatus_TrainerAlreadyInDesiredState() {
        String username = "Kushtar.Amalbekov";
        boolean status = true;
        Trainer trainer = new Trainer();
        trainer.setUser(new User()); // Initialize the User object
        trainer.getUser().setIsActive(true);
        when(trainerRepository.findByUserUsername(username)).thenReturn(Optional.of(trainer));

        assertThrows(IllegalArgumentException.class, () -> trainerService.updateTrainerStatus(status, username));
    }
    @Test
    void testGetTrainerTrainingsByCriteria_TrainingsNotFound() {
        TrainerTrainingsRequest request = new TrainerTrainingsRequest(
                "Eulan.Ibraimov",
                null,
                null,
                "Kanysh.Abdyrakmanova"
        );
        when(trainerRepository.existsByUserUsername(request.getUsername())).thenReturn(true);
        when(trainerRepository.getTrainerTrainingsByCriteria(
                eq(request.getUsername()),
                eq(request.getPeriodFrom()),
                eq(request.getPeriodTo()),
                eq(request.getTraineeName())
        )).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainerService.getTrainerTrainingsByCriteria(request));
    }
}
