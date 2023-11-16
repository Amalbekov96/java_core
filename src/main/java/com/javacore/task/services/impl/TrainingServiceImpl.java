package com.javacore.task.services.impl;

import com.javacore.task.entities.Training;
import com.javacore.task.enums.ErrorCode;
import com.javacore.task.exceptions.ApiException;
import com.javacore.task.exceptions.StorageException;
import com.javacore.task.mappers.TrainingMapper;
import com.javacore.task.models.TrainingModel;
import com.javacore.task.repositories.TrainingRepository;
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
            Training trainingEntity = trainingMapper.trainingModelToTraining(trainingModel);
            trainingEntity = trainingRepository.save(trainingEntity);
            return trainingMapper.trainingToTrainingModel(trainingEntity);
        } catch (Exception | StorageException e) {
            log.error("Error creating Training", e);
            throw new ApiException("Error creating Training", ErrorCode.TRAINING_NOT_FOUND);
        }
    }

    @Override
    public TrainingModel updateTraining(Long trainingId, TrainingModel trainingModel) {
        try {
            Training existingTraining = trainingRepository.findById(trainingId);
            if(existingTraining == null) {
                   new ApiException("Training with ID " + trainingId + " not found", ErrorCode.TRAINING_NOT_FOUND);
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
