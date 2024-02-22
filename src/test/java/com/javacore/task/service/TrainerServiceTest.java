package com.javacore.task.service;
import static org.mockito.Mockito.*;

import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.Training;
import com.javacore.task.entities.User;
import com.javacore.task.mappers.TrainerMapper;
import com.javacore.task.mappers.TrainingMapper;
import com.javacore.task.models.request.TrainerTrainingsRequest;
import com.javacore.task.models.request.TrainerUpdateRequest;
import com.javacore.task.models.response.TrainerTrainingInfoResponse;
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
public class TrainerServiceTest {
    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainerMapper trainerMapper;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Mock
    private TrainingMapper trainingDTOMapper;

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
                eq(request.username()),
                eq(request.periodFrom()),
                eq(request.periodTo()),
                eq(request.traineeName())
        )).thenReturn(trainings);

        List<TrainerTrainingInfoResponse> result = trainerService.getTrainerTrainingsByCriteria(request);

        verify(trainerRepository, times(1)).getTrainerTrainingsByCriteria(
                eq(request.username()),
                eq(request.periodFrom()),
                eq(request.periodTo()),
                eq(request.traineeName())
        );

        assertEquals(0, result.size());
    }

}
