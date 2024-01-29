package com.javacore.task.services.impl;

import com.javacore.task.entities.Trainee;
import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.Training;
import com.javacore.task.enums.ErrorCode;
import com.javacore.task.exceptions.ApiException;
import com.javacore.task.exceptions.UserNotFoundException;
import com.javacore.task.mappers.TrainingMapper;
import com.javacore.task.models.TrainingModel;
import com.javacore.task.models.request.TrainingRequest;
import com.javacore.task.repositories.TraineeRepository;
import com.javacore.task.repositories.TrainerRepository;
import com.javacore.task.repositories.TrainingRepository;
import com.javacore.task.services.TrainingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingMapper trainingMapper;
    private final TraineeRepository traineeRepository;
    @Override
    public TrainingModel getTrainingById(Long trainingId) {
        Training training = trainingRepository.findById(trainingId).orElseThrow(
                () -> new ApiException(String.format("Training with id: %d not found", trainingId), ErrorCode.TRAINING_NOT_FOUND));
        if (training != null) {
            return trainingMapper.trainingToTrainingModel(training);
        } else {
            throw new ApiException("Training with ID " + trainingId + " not found", ErrorCode.TRAINING_NOT_FOUND);
        }
    }
    @Override
    public TrainingModel updateTraining(Long trainingId, TrainingModel trainingModel) {
        try {
            Training existingTraining = trainingRepository.findById(trainingId).orElseThrow(
                    () -> new ApiException(String.format("Training with id: %d not found", trainingId), ErrorCode.TRAINING_NOT_FOUND));
            if(existingTraining == null) {
                throw new ApiException("Training with ID " + trainingId + " not found", ErrorCode.TRAINING_NOT_FOUND);
            }
            trainingMapper.update(trainingModel, existingTraining);
            existingTraining = trainingRepository.save(existingTraining);

            return trainingMapper.trainingToTrainingModel(existingTraining);
        } catch (Exception e) {
            log.error("Error updating Training", e);
            throw new ApiException("Error updating Training", ErrorCode.TRAINING_NOT_FOUND);
        }
    }

    @Override
    public void deleteTraining(Long trainingId) {
        try {
            trainingRepository.deleteById(trainingId);
        } catch (Exception e) {
            log.error("Error deleting Training", e);
            throw new ApiException("Error deleting Training", ErrorCode.TRAINING_NOT_FOUND);
        }
    }

    @Override
    public void saveTraining(TrainingRequest training) {
        Trainee trainee = traineeRepository.findTraineeByUserUsername(training.traineeUsername()).orElseThrow(
                () -> {
                    log.warn("Response: Trainee not found");
                    return new UserNotFoundException("Trainee not found");
                }
        );
        Trainer trainer = trainerRepository.findByUserUsername(training.trainerUsername()).orElseThrow(
                () -> {
                    log.warn("Response: Trainer not found");
                    return new UserNotFoundException("Trainer not found");
                });
        trainee.getTrainers().add(trainer);
        Training savedtraining = Training.builder()
                .trainingName(training.trainingName())
                .trainingDate(training.trainingDate())
                .trainingType(trainer.getSpecialization())
                .trainingDuration(training.duration())
                .trainer(trainer)
                .trainee(trainee)
                .build();
        trainingRepository.save(savedtraining);
        traineeRepository.save(trainee);

        log.info("Added Training: {}", training);

    }

}
