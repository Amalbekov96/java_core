package com.javacore.task.services.impl;

import com.javacore.task.entities.Training;
import com.javacore.task.enums.ErrorCode;
import com.javacore.task.exceptions.ApiException;
import com.javacore.task.exceptions.EntityIsNotFound;
import com.javacore.task.exceptions.StorageException;
import com.javacore.task.mappers.TrainingMapper;
import com.javacore.task.models.TraineeModel;
import com.javacore.task.models.TrainerModel;
import com.javacore.task.models.TrainingModel;
import com.javacore.task.repositories.TrainingRepository;
import com.javacore.task.services.TraineeService;
import com.javacore.task.services.TrainerService;
import com.javacore.task.services.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Override
    public TrainingModel getTrainingById(Long trainingId) {
        Training training = trainingRepository.findById(trainingId);
        if (training != null) {
            return trainingMapper.trainingToTrainingModel(training);
        } else {
            throw new ApiException("Training with ID " + trainingId + " not found", ErrorCode.TRAINING_NOT_FOUND);
        }
    }

    @Override
    public TrainingModel createTraining(TrainingModel trainingModel) {
        try {
            // Check if Trainer information is provided
            if (trainingModel.getTrainer() == null || trainingModel.getTrainer().getId() == null) {
                throw new ApiException("Trainer is not created yet", ErrorCode.TRAINER_NOT_FOUND);
            }

            // Check if Trainee information is provided
            if (trainingModel.getTrainee() == null || trainingModel.getTrainee().getTraineeId() == null) {
                throw new ApiException("Trainee is not created yet", ErrorCode.TRAINEE_NOT_FOUND);
            }

            // Retrieve Trainer information
            TrainerModel trainerModel = trainerService.getTrainerById(trainingModel.getTrainer().getId());

            // Check if Trainer exists
            if (trainerModel == null) {
                throw new ApiException("Trainer with ID " + trainingModel.getTrainer().getId() + " not found", ErrorCode.TRAINER_NOT_FOUND);
            }

            // Retrieve Trainee information
            TraineeModel traineeModel = traineeService.getTraineeById(trainingModel.getTrainee().getTraineeId());

            // Check if Trainee exists
            if (traineeModel == null) {
                throw new ApiException("Trainee with ID " + trainingModel.getTrainee().getTraineeId() + " not found", ErrorCode.TRAINEE_NOT_FOUND);
            }

            // Set Trainer and Trainee information in the TrainingModel
            trainingModel.setTrainer(trainerModel);
            trainingModel.setTrainee(traineeModel);

            // Map TrainingModel to Training entity
            Training trainingEntity = trainingMapper.trainingModelToTraining(trainingModel);

            // Save the Training entity
            trainingEntity = trainingRepository.save(trainingEntity);

            // Map the saved Training entity back to TrainingModel
            return trainingMapper.trainingToTrainingModel(trainingEntity);

        } catch (StorageException e) {
            log.error(String.format("Error creating Training due to %s", e.getMessage()));
            throw new ApiException(e.getMessage(), ErrorCode.TRAINING_NOT_FOUND);
        }
    }


    @Override
    public TrainingModel updateTraining(Long trainingId, TrainingModel trainingModel) {
        try {
            Training existingTraining = trainingRepository.findById(trainingId);
            if(existingTraining == null) {
                throw new ApiException("Training with ID " + trainingId + " not found", ErrorCode.TRAINING_NOT_FOUND);
            }
            trainingMapper.update(trainingModel, existingTraining);
            existingTraining = trainingRepository.save(existingTraining);

            return trainingMapper.trainingToTrainingModel(existingTraining);
        } catch (Exception | StorageException e) {
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
}
