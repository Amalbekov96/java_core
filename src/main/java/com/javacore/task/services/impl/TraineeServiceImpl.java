package com.javacore.task.services.impl;

import com.javacore.task.entities.Trainee;
import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.Training;
import com.javacore.task.enums.ErrorCode;
import com.javacore.task.enums.TrainingTypes;
import com.javacore.task.exceptions.ApiException;
import com.javacore.task.exceptions.UserNotFoundException;
import com.javacore.task.mappers.TraineeMapper;
import com.javacore.task.mappers.TrainingMapper;
import com.javacore.task.models.*;
import com.javacore.task.models.request.TraineeProfileUpdateResponse;
import com.javacore.task.models.request.TraineeTrainingsRequest;
import com.javacore.task.models.request.TraineeUpdateRequest;
import com.javacore.task.models.response.TraineeInfoResponse;
import com.javacore.task.models.response.TrainersListResponse;
import com.javacore.task.models.response.TraineeTrainingInfoResponse;
import com.javacore.task.repositories.TraineeRepository;
import com.javacore.task.repositories.TrainerRepository;
import com.javacore.task.services.TraineeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final TraineeMapper traineeMapper;
    private final TrainingMapper trainingMapper;
    private final TrainerRepository trainerDao;

    @Override
    public TraineeModel getTraineeById(Long traineeId) {
        Trainee trainee = traineeRepository.findById(traineeId).orElseThrow(
                () -> new UserNotFoundException(String.format("Trainee with id: %d not found", traineeId)));
        if(trainee != null) {
            return traineeMapper.traineeToTraineeModel(trainee);
        } else {
            throw new ApiException("Trainee with ID " + traineeId + " not found", ErrorCode.TRAINER_NOT_FOUND);
        }
    }

    @Override
    public TraineeProfileUpdateResponse updateTrainee(TraineeUpdateRequest request) {
        try {
            Trainee existingTrainee = traineeRepository.findTraineeByUserUsername(request.userName()).orElseThrow(
                    () -> new UserNotFoundException(String.format("Trainee with username: %s not found", request.userName())));
            if(existingTrainee == null) {
                throw new ApiException("Trainee with username " + request.userName() + " not found", ErrorCode.TRAINER_NOT_FOUND);
            }

            Trainee trainee = traineeMapper.update(request, existingTrainee);
            traineeRepository.save(trainee);

            return traineeMapper.traineeToTraineeResponse(existingTrainee);
        } catch (Exception e) {
            log.error("Error updating Trainee", e);
            throw new ApiException("Error updating Trainee", ErrorCode.TRAINER_NOT_FOUND);
        }
    }

    @Override
    public void deleteTrainee(String username) {
        try {
            Trainee trainee = traineeRepository.findTraineeByUserUsername(username).orElseThrow(
                    () -> new UserNotFoundException(String.format("Trainee with username: %s not found", username)));
            traineeRepository.deleteById(trainee.getTraineeId());
            log.info("Trainee deleted with id: {}", trainee.getTraineeId());
        } catch (Exception e) {
            log.error("Error deleting Trainee", e);
            throw new ApiException("Error deleting Trainee", ErrorCode.TRAINER_NOT_FOUND);
        }
    }

        @Override
        public TraineeInfoResponse findTraineeProfileByUsername(String username) {
            Trainee trainee = traineeRepository.findTraineeByUserUsername(username).orElseThrow(()->{
                log.warn("Response: Trainee not found");
                return new UserNotFoundException("Trainee not found");
            });
            log.info("Retrieved Trainee profile by username: {}, Trainee: {}", username, trainee);
            return traineeMapper.traineeInfoResponse(trainee);
        }


    @Override
    public String updateTraineeStatus(boolean status, String username) {

           Trainee trainee = traineeRepository.findTraineeByUserUsername(username).orElseThrow(()->{
                log.warn("Response: Trainee not found");
                return new UserNotFoundException("Trainee not found");
            });

            if (status && !trainee.getUser().getIsActive()){
                traineeRepository.activateTrainee(trainee.getTraineeId());
                log.info("Activated Trainee with id: {}", trainee.getTraineeId());
                return "Activated";
            } else if (!status && trainee.getUser().getIsActive()) {
                traineeRepository.deactivateTrainee(trainee.getTraineeId());
                log.info("Deactivated Trainee with id: {}", trainee.getTraineeId());
                return "Deactivated";
            } else {
                log.info("Trainee with id {} is already in the desired state", trainee.getTraineeId());
                throw  new IllegalArgumentException("Trainee is already in the desired state");
            }
        }

    @Override
    public List<TrainersListResponse> updateTraineeTrainersList(String username, List<String> trainersUsernames) {
         Trainee trainee = traineeRepository.findTraineeByUserUsername(username).orElseThrow(()->{
             log.warn("Response: Trainee not found");
             return new UserNotFoundException("Trainee not found");
         });
         List<Trainer> trainers = traineeRepository.getNotAssignedTrainers(trainee.getUser().getUsername());
         List<Trainer> addedTrainers= trainerDao.findTrainersByUserUserName(trainersUsernames);
         trainers.addAll(addedTrainers);
         trainee.setTrainers(trainers);
         traineeRepository.save(trainee);
         log.info("Updated Trainer list for Trainee with username : {}", username);
         return traineeMapper.mapTraineesTrainersToDto(trainers);
        }

    @Override
    public List<TrainersListResponse> getNotAssignedActiveTrainersListForTrainee(String username) {
         Trainee trainee = traineeRepository.findTraineeByUserUsername(username).orElseThrow(()->{
             log.warn("Response: Trainee not found");
             return new UserNotFoundException("Trainee not found");
         });
         List<Trainer> trainers = traineeRepository.getNotAssignedTrainers(trainee.getUser().getUsername());
         log.info("Retrieved not assigned active trainers list for Trainee with username {}: {}", username, trainers);
         return traineeMapper.mapTraineesTrainersToDto(trainers);
        }

    @Override
    public List<TraineeTrainingInfoResponse> getTraineeTrainingsByCriteria(TraineeTrainingsRequest request) {
        log.info("Retrieving Trainee Trainings by : Criteria: {}", request);
         List<Training> trainings = traineeRepository.getTraineeTrainingsByCriteria(request.traineeName(),
             request.periodFrom(), request.periodTo(), request.trainerName(), TrainingTypes.valueOf(request.trainingType()));
            log.info("Retrieved Trainee Trainings by : Criteria: {}, Trainings: {}",request, trainings);
            return trainingMapper.mapTraineeTrainingsToDto(trainings);
        }
    }

