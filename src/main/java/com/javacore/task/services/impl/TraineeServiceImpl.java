package com.javacore.task.services.impl;

import com.javacore.task.entities.Trainee;
import com.javacore.task.enums.ErrorCode;
import com.javacore.task.exceptions.ApiException;
import com.javacore.task.exceptions.StorageException;
import com.javacore.task.mappers.TraineeMapper;
import com.javacore.task.models.TraineeModel;
import com.javacore.task.repositories.TraineeRepository;
import com.javacore.task.services.TraineeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final TraineeMapper traineeMapper;

    @Override
    public TraineeModel getTraineeById(Long traineeId) {
        Trainee trainee = traineeRepository.findById(traineeId);
        if(trainee != null) {
            return traineeMapper.traineeToTraineeModel(trainee);
        } else {
            throw new ApiException("Trainee with ID " + traineeId + " not found", ErrorCode.TRAINER_NOT_FOUND);
        }
    }

    @Override
    public TraineeModel createTrainee(TraineeModel traineeModel) {
        try {
            Trainee traineeEntity = traineeMapper.traineeModelToTrainee(traineeModel);
            traineeEntity = traineeRepository.save(traineeEntity);
            return traineeMapper.traineeToTraineeModel(traineeEntity);
        } catch (Exception | StorageException e) {
            log.error("Error creating Trainee", e);
            throw new ApiException("Error creating Trainee", ErrorCode.TRAINER_NOT_FOUND);
        }
    }

    @Override
    public TraineeModel updateTrainee(Long traineeId, TraineeModel traineeModel) {
        try {
            Trainee existingTrainee = traineeRepository.findById(traineeId);
            if(existingTrainee == null) {
                throw new ApiException("Trainee with ID " + traineeId + " not found", ErrorCode.TRAINER_NOT_FOUND);
            }

            traineeMapper.update(traineeModel, existingTrainee);
            Trainee savedTrainee = traineeRepository.save(existingTrainee);

            return traineeMapper.traineeToTraineeModel(savedTrainee);
        } catch (Exception | StorageException e) {
            log.error("Error updating Trainee", e);
            throw new ApiException("Error updating Trainee", ErrorCode.TRAINER_NOT_FOUND);
        }
    }

    @Override
    public void deleteTrainee(Long traineeId) {
        try {
            traineeRepository.deleteById(traineeId);
        } catch (Exception e) {
            log.error("Error deleting Trainee", e);
            throw new ApiException("Error deleting Trainee", ErrorCode.TRAINER_NOT_FOUND);
        }
    }

    @Override
    public List<TraineeModel> findAll() {
        try {
            List<Trainee> trainees = traineeRepository.findAll();
            return  traineeMapper.traineesToTraineeModels(trainees);
        } catch (Exception e) {
            log.error("Error creating Trainee", e);
            throw new ApiException("Error creating Trainee", ErrorCode.TRAINER_NOT_FOUND);
        }
    }
}
