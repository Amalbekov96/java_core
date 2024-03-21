package com.javacore.task.services.impl;

import com.javacore.task.entities.Trainee;
import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.Training;
import com.javacore.task.enums.WorkHourCalculateType;
import com.javacore.task.exceptions.UserNotFoundException;
import com.javacore.task.mappers.TrainingMapper;
import com.javacore.task.models.request.TrainingRequest;
import com.javacore.task.models.response.TraineeTrainingInfoResponse;
import com.javacore.task.repositories.TraineeRepository;
import com.javacore.task.repositories.TrainerRepository;
import com.javacore.task.repositories.TrainingRepository;
import com.javacore.task.services.TrainingService;
import com.javacore.task.services.WorkHourCalculatorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final WorkHourCalculatorService workHourCalculatorService;

    @Transactional
    @Override
    public void saveTraining(TrainingRequest training) {
        Trainee trainee = traineeRepository.findTraineeByUserUsername(training.getTraineeUsername()).orElseThrow(
                () -> {
                    log.warn("Response: Trainee not found");
                    throw new UserNotFoundException("Trainee not found");
                }
        );
        Trainer trainer = trainerRepository.findByUserUsername(training.getTrainerUsername()).orElseThrow(
                () -> {
                    log.warn("Response: Trainer not found");
                    throw new UserNotFoundException("Trainer not found");
                });
        if (trainee.getTrainers()==null){
            trainee.setTrainers(new ArrayList<>());
        }
        trainee.getTrainers().add(trainer);
        Training savedtraining = Training.builder()
                .trainingName(training.getTrainingName())
                .trainingDate(training.getTrainingDate())
                .trainingType(trainer.getSpecialization())
                .trainingDuration(training.getDuration())
                .trainer(trainer)
                .trainee(trainee)
                .build();
        trainingRepository.save(savedtraining);
        traineeRepository.save(trainee);
        workHourCalculatorService.create(savedtraining, WorkHourCalculateType.ADD);
        log.info("Added Training: {}", training);
    }
}
