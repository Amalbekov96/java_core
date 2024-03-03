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
import com.javacore.task.models.response.TraineeInfoResponse;
import com.javacore.task.models.TraineeModel;
import com.javacore.task.models.request.TraineeTrainingsRequest;
import com.javacore.task.models.request.TraineeUpdateRequest;
import com.javacore.task.models.response.TraineeProfileUpdateResponse;
import com.javacore.task.models.response.TraineeTrainingInfoResponse;
import com.javacore.task.models.response.TrainersListResponse;
import com.javacore.task.repositories.TraineeRepository;
import com.javacore.task.repositories.TrainerRepository;
import com.javacore.task.repositories.TrainingRepository;
import com.javacore.task.services.TraineeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final TraineeMapper traineeMapper;
    private final TrainingMapper trainingMapper;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;

    @Override
    public TraineeModel getTraineeById(Long traineeId) {
        Trainee trainee = traineeRepository.findById(traineeId).orElseThrow(
                () -> new UserNotFoundException(String.format("Trainee with id: %d not found", traineeId)));
        log.info("Retrieved Trainee by id: {}, Trainee: {}", traineeId, trainee);
        if(trainee != null) {
            return traineeMapper.traineeToTraineeModel(trainee);
        } else {
            throw new ApiException("Trainee with ID " + traineeId + " not found", ErrorCode.TRAINER_NOT_FOUND);
        }
    }

    @Transactional
    @Override
    public TraineeProfileUpdateResponse updateTrainee(TraineeUpdateRequest request) {

            Trainee existingTrainee = traineeRepository.findTraineeByUserUsername(request.getUserName()).orElseThrow(
                    () -> new UserNotFoundException(String.format("Trainee with username: %s not found", request.getUserName())));

            Trainee trainee = traineeMapper.update(request, existingTrainee);
            traineeRepository.save(trainee);

            return traineeMapper.traineeToTraineeResponse(existingTrainee);

    }

    @Transactional
    @Override
    public void deleteTrainee(String username) {

            Trainee trainee = traineeRepository.findTraineeByUserUsername(username).orElseThrow(
                    () -> new UserNotFoundException(String.format("Trainee with username: %s not found", username)));
            traineeRepository.deleteById(trainee.getTraineeId());
            log.info("Trainee deleted with id: {}", trainee.getTraineeId());
    }

    @Override
    public TraineeInfoResponse findTraineeProfileByUsername(String username) {
        Trainee trainee = traineeRepository.findTraineeByUserUsername(username).orElseThrow(()->{
            log.warn("Response: Trainee not found");
            throw  new UserNotFoundException("Trainee not found");
        });
        log.info("Retrieved Trainee profile by username: {}, Trainee: {}", username, trainee);
        return traineeMapper.traineeInfoResponse(trainee);
    }


    @Transactional
    @Override
    public String updateTraineeStatus(boolean status, String username) {

           Trainee trainee = traineeRepository.findTraineeByUserUsername(username).orElseThrow(()->{
                log.warn("Response: Trainee not found");
                throw  new UserNotFoundException("Trainee not found");
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

    @Transactional
    @Override
    public List<TrainersListResponse> updateTraineeTrainersList(String username, List<String> trainersUsernames) {
         Trainee trainee = traineeRepository.findTraineeByUserUsername(username).orElseThrow(()->{
             log.warn("Response: Trainee not found");
             return new UserNotFoundException("Trainee not found");
         });
         List<Trainer> trainers = traineeRepository.getNotAssignedTrainers(trainee.getUser().getUsername());
         List<Trainer> addedTrainers= trainerRepository.findTrainersByUserUserName(trainersUsernames);

        boolean isTrainerListValid = addedTrainers.stream()
                .allMatch(trainer -> trainers.stream()
                        .anyMatch(existingTrainer -> existingTrainer.getUser().getUsername().equals(trainer.getUser().getUsername())));
        if(isTrainerListValid){
            trainee.getTrainers().addAll(addedTrainers);
        }

        for (Trainer trainer : addedTrainers) {
            Training newTraining = Training.builder()
                    .trainee(trainee)
                    .trainer(trainer)
                    .trainingType(trainer.getSpecialization())
                    .trainingName(String.format("Training with %s and %s", trainer.getUser().getUsername(), trainee.getUser().getUsername()))
                    .trainingDuration(0)
                    .trainingDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .build();
            trainingRepository.save(newTraining);
            trainee.getTrainings().add(newTraining);
        }
         traineeRepository.save(trainee);

         log.info("Updated Trainer list for Trainee with username : {}", username);

         return addedTrainers.stream()
                 .map(trainer -> new TrainersListResponse(
                         trainer.getUser().getUsername(),
                         trainer.getUser().getFirstName(),
                         trainer.getUser().getLastName(),
                         trainer.getSpecialization().getTrainingType().name()
                 )).distinct().toList();
        }

    @Override
    public List<TrainersListResponse> getNotAssignedActiveTrainersListForTrainee(String username) {
         Trainee trainee = traineeRepository.findTraineeByUserUsername(username).orElseThrow(()->{
             log.warn("Response: Trainee not found");
            throw new UserNotFoundException("Trainee not found");
         });
         List<Trainer> trainers = traineeRepository.getNotAssignedTrainers(trainee.getUser().getUsername());
         log.info("Retrieved not assigned active trainers list for Trainee with username {}: {}", username, trainers);
         return traineeMapper.mapTraineesTrainersToDto(trainers);
        }

    @Override
    public List<TraineeTrainingInfoResponse> getTraineeTrainingsByCriteria(TraineeTrainingsRequest request) {

        log.info("Retrieving Trainee Trainings by : Criteria: {}", request);
        if (traineeRepository.existsByUserUsername(request.getTraineeName())) {
            List<Training> trainings = traineeRepository.getTraineeTrainingsByCriteria(request.getTraineeName(),
                    request.getPeriodFrom(), request.getPeriodTo(), request.getTrainerName(), TrainingTypes.valueOf(request.getTrainingType())).orElseThrow(
                    () -> new UserNotFoundException("Data not found"));

            log.info("Retrieved Trainee Trainings by : Criteria: {}, Trainings: {}", request, trainings);
            return trainingMapper.mapTraineeTrainingsToDto(trainings);
        } else {
            throw new UserNotFoundException("Trainee not found");
        }
    }
    }

