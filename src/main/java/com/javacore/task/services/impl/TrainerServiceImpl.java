package com.javacore.task.services.impl;

import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.Training;
import com.javacore.task.enums.ErrorCode;
import com.javacore.task.exceptions.ApiException;
import com.javacore.task.exceptions.BadCredentialsException;
import com.javacore.task.exceptions.UserNotFoundException;
import com.javacore.task.mappers.TrainerMapper;
import com.javacore.task.mappers.TrainingMapper;
import com.javacore.task.models.*;
import com.javacore.task.models.request.TrainerTrainingsRequest;
import com.javacore.task.models.response.TrainerInfoResponse;
import com.javacore.task.models.response.TrainingInfoResponse;
import com.javacore.task.repositories.TrainerRepository;
import com.javacore.task.repositories.TrainingRepository;
import com.javacore.task.services.TrainerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TrainerServiceImpl implements TrainerService {

    private final TrainerMapper trainerMapper;
    private final PasswordEncoder passwordEncoder;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingDTOMapper;


    @Override
    public TrainerModel getTrainerById(Long trainerId) {
        Trainer trainer = trainerRepository.findById(trainerId).orElseThrow(
                () -> new UserNotFoundException(String.format("Trainer with id: %d not found", trainerId)));
        if(trainer != null) {
            log.info("Retrieved Trainer by id: {}, Trainer: {}", trainerId, trainer);
            return trainerMapper.trainerToTrainerModel(trainer);
        } else {
            throw new ApiException("Trainer with ID " + trainerId + " not found", ErrorCode.TRAINER_NOT_FOUND);
        }
    }

    @Override
    public TrainerModel createTrainer(TrainerModel trainerModel) {
        try {
            Trainer trainerEntity = trainerMapper.trainerModelToTrainer(trainerModel);
            trainerEntity = trainerRepository.save(trainerEntity);
            return trainerMapper.trainerToTrainerModel(trainerEntity);
        } catch (Exception e) {
            log.error("Error creating Trainer", e);
            throw new ApiException("Error creating Trainer", ErrorCode.TRAINER_NOT_FOUND);
        }
    }

    @Override
    public TrainerModel updateTrainer(Long trainerId, TrainerModel trainerModel) {
        try {
            Trainer existingTrainer = trainerRepository.findById(trainerId).orElseThrow(
                    () -> new UserNotFoundException(String.format("Trainer with id: %d not found", trainerId)));
            if(existingTrainer == null)
                throw new ApiException("Trainer with ID " + trainerId + " not found", ErrorCode.TRAINER_NOT_FOUND);

            trainerMapper.update(trainerModel, existingTrainer);
            existingTrainer = trainerRepository.save(existingTrainer);

            return trainerMapper.trainerToTrainerModel(existingTrainer);
        } catch (Exception e) {
            log.error("Error updating Trainer", e);
            throw new ApiException("Error updating Trainer", ErrorCode.TRAINER_NOT_FOUND);
        }
    }

    @Override
    public void deleteTrainer(Long trainerId) {
        try {
            List<Training> trainings = trainingRepository.findByTrainerId(trainerId);
            trainings.forEach(training -> trainingRepository.deleteById(training.getId()));
            trainerRepository.deleteById(trainerId);
            log.info("Deleted Trainer with id: {}", trainerId);
        } catch (Exception e) {
            log.error("Error deleting Trainer", e);
            throw new ApiException("Error deleting Trainer", ErrorCode.TRAINER_NOT_FOUND);
        }
    }

    @Override
    public TrainerInfoResponse findTrainerProfileByUsername(String username) {
        Trainer trainer = trainerRepository.findByUserUsername(username).orElseThrow(()->{
            log.warn("Response: Trainer not found");
            return new UserNotFoundException("Trainer not found");
        });
        log.info("Selected Trainer profile by username: {}, Trainer: {}", username, trainer);
        return trainerMapper.trainerInfoResponse(trainer);
    }

    @Override
    public void changeTrainerPassword(long id, String password, String newPassword) {
        Trainer trainer = trainerRepository.findById(id).orElseThrow(()-> {
            log.warn("Response: Trainer not found");
            return new UserNotFoundException(String.format("Trainer with id: %d not found", id));
        });
        if (!passwordEncoder.matches(password, trainer.getUser().getPassword())) {
            throw new BadCredentialsException("wrong password");
        }
        trainerRepository.changeTrainerPassword(id, passwordEncoder.encode(newPassword));
        log.info("Changed password for Trainer");
    }

    @Override
    public String updateTrainerStatus(boolean status, long trainerId) {
        boolean isActive = trainerRepository.findById(trainerId).orElseThrow(()-> {
            log.warn("Response: Trainer not found");
            return new UserNotFoundException(String.format("Trainer with id: %d not found", trainerId));
        }).getUser().getIsActive();
        if (status && !isActive){
            trainerRepository.activateTrainer(trainerId);
            log.info("Activated Trainee with id: {}", trainerId);
            return "Activated";
        } else if (!status && isActive) {
            trainerRepository.deactivateTrainer(trainerId);
            log.info("Deactivated Trainee with id: {}", trainerId);
            return "Deactivated";
        } else {
            log.info("Trainee with id {} is already in the desired state", trainerId);
            throw new IllegalArgumentException("Trainer is already in the desired state");
        }
    }
    @Override
    public List<TrainingInfoResponse> getTrainerTrainingsByCriteria(TrainerTrainingsRequest request) {
        List<Training> trainings = trainerRepository.getTrainerTrainingsByCriteria(request.username(),request.periodFrom(),request.periodTo(),request.traineeName());
        log.info("Retrieved Trainer Trainings by Criteria: {}, Trainings: {}",request, trainings);
        return trainingDTOMapper.mapTrainingsToDto(trainings);
    }
}
