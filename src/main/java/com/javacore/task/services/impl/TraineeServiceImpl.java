package com.javacore.task.services.impl;

import com.javacore.task.entities.Trainee;
import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.Training;
import com.javacore.task.enums.ErrorCode;
import com.javacore.task.exceptions.ApiException;
import com.javacore.task.exceptions.UserNotFoundException;
import com.javacore.task.mappers.TraineeMapper;
import com.javacore.task.mappers.TrainingMapper;
import com.javacore.task.models.*;
import com.javacore.task.models.request.TraineeTrainingsRequest;
import com.javacore.task.models.response.TraineeInfoResponse;
import com.javacore.task.models.response.TrainersListResponse;
import com.javacore.task.models.response.TrainingInfoResponse;
import com.javacore.task.repositories.TraineeRepository;
import com.javacore.task.repositories.TrainerRepository;
import com.javacore.task.repositories.TrainingRepository;
import com.javacore.task.services.TraineeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final PasswordEncoder passwordEncoder;
    private final TraineeRepository traineeRepository;
    private final TraineeMapper traineeMapper;
    private final TrainingMapper trainingMapper;
    private final TrainingRepository trainingRepo;
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
    public TraineeModel createTrainee(TraineeModel traineeModel) {
        try {
            Trainee traineeEntity = traineeMapper.traineeModelToTrainee(traineeModel);
            traineeEntity = traineeRepository.save(traineeEntity);
            return traineeMapper.traineeToTraineeModel(traineeEntity);
        } catch (Exception e) {
            log.error("Error creating Trainee", e);
            throw new ApiException("Error creating Trainee", ErrorCode.TRAINER_NOT_FOUND);
        }
    }

    @Override
    public TraineeModel updateTrainee(Long traineeId, TraineeModel traineeModel) {
        try {
            Trainee existingTrainee = traineeRepository.findById(traineeId).orElseThrow(
                    () -> new UserNotFoundException(String.format("Trainee with id: %d not found", traineeId)));
            if(existingTrainee == null) {
                throw new ApiException("Trainee with ID " + traineeId + " not found", ErrorCode.TRAINER_NOT_FOUND);
            }

            traineeMapper.update(traineeModel, existingTrainee);
            traineeRepository.save(existingTrainee);

            return traineeMapper.traineeToTraineeModel(existingTrainee);
        } catch (Exception e) {
            log.error("Error updating Trainee", e);
            throw new ApiException("Error updating Trainee", ErrorCode.TRAINER_NOT_FOUND);
        }
    }

    @Override
    public void deleteTrainee(Long traineeId) {
        try {
            List<Training> trainings = trainingRepo.findByTrainerId(traineeId);
            trainings.forEach(training -> trainingRepo.deleteById(training.getId()));
            traineeRepository.deleteById(traineeId);
            log.info("Trainee deleted with id: {}", traineeId);
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
        public void changeTraineePassword(long id,String password, String newPassword) {
            Trainee trainee = traineeRepository.findById(id).orElseThrow(()-> {
                log.warn("Response: Trainee not found");
                return new UserNotFoundException(String.format("Trainee with id: %d not found", id));
            });
            if (!passwordEncoder.matches(password, trainee.getUser().getPassword())) {
                throw new org.springframework.security.authentication.BadCredentialsException("wrong password");
            }
            traineeRepository.changeTraineePassword(id,passwordEncoder.encode(newPassword));
            log.info("Changed password for Trainee with id: {}", id);
        }

    @Override
    public String updateTraineeStatus(boolean status, long traineeId) {
            boolean isActive = traineeRepository.findById(traineeId).orElseThrow(()-> {
                log.warn("Response: Trainee not found");
                return new UserNotFoundException(String.format("Trainee with id: %d not found", traineeId));
            }).getUser().getIsActive();
            if (status && !isActive){
                traineeRepository.activateTrainee(traineeId);
                log.info("Activated Trainee with id: {}", traineeId);
                return "Activated";
            } else if (!status && isActive) {
                traineeRepository.deactivateTrainee(traineeId);
                log.info("Deactivated Trainee with id: {}", traineeId);
                return "Deactivated";
            } else {
                log.info("Trainee with id {} is already in the desired state", traineeId);
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
    public List<TrainingInfoResponse> getTraineeTrainingsByCriteria(TraineeTrainingsRequest request) {
         List<Training> trainings = traineeRepository.getTraineeTrainingsByCriteria(request.traineeUsername(),
             request.periodFrom(), request.periodTo(), request.trainerName(),request.trainingType());
            log.info("Retrieved Trainee Trainings by : Criteria: {}, Trainings: {}",request, trainings);
            return trainingMapper.mapTrainingsToDto(trainings);
        }
    }

