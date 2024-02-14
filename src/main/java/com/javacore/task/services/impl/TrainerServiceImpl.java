package com.javacore.task.services.impl;

import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.Training;
import com.javacore.task.enums.ErrorCode;
import com.javacore.task.exceptions.ApiException;
import com.javacore.task.exceptions.UserNotFoundException;
import com.javacore.task.mappers.TrainerMapper;
import com.javacore.task.mappers.TrainingMapper;
import com.javacore.task.models.*;
import com.javacore.task.models.request.TrainerTrainingsRequest;
import com.javacore.task.models.request.TrainerUpdateRequest;
import com.javacore.task.models.response.TrainerInfoResponse;
import com.javacore.task.models.response.TrainerTrainingInfoResponse;
import com.javacore.task.models.response.TrainerUpdateResponse;
import com.javacore.task.repositories.TrainerRepository;
import com.javacore.task.services.TrainerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TrainerServiceImpl implements TrainerService {

    private final TrainerMapper trainerMapper;
    private final TrainerRepository trainerRepository;
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

    @Transactional
    @Override
    public TrainerUpdateResponse updateTrainer(TrainerUpdateRequest request) {
        try {
            Trainer existingTrainer = trainerRepository.findByUserUsername(request.userName()).orElseThrow(
                    () -> new UserNotFoundException("Trainer not found"));

           Trainer trainer =  trainerMapper.update(request, existingTrainer);
            trainerRepository.save(trainer);
            return trainerMapper.trainerToTrainerUpdateResponse(trainer);
        } catch (Exception e) {
            log.error("Error updating Trainer", e);
            throw new ApiException("Error updating Trainer", ErrorCode.TRAINER_NOT_FOUND);
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
    public String updateTrainerStatus(boolean status, String username) {
        Trainer trainer = trainerRepository.findByUserUsername(username).orElseThrow(()->{
            log.warn("Response: Trainee not found");
            return new UserNotFoundException("Trainee not found");
        });

        if (status && !trainer.getUser().getIsActive()){
            trainerRepository.activateTrainer(trainer.getId());
            log.info("Activated Trainer with id: {}", trainer.getId());
            return "Activated";
        } else if (!status && trainer.getUser().getIsActive()) {
            trainerRepository.deactivateTrainer(trainer.getId());
            log.info("Deactivated Trainer with id: {}", trainer.getId());
            return "Deactivated";
        } else {
            log.info("Trainee with id {} is already in the desired state", trainer.getId());
            throw  new IllegalArgumentException("Trainee is already in the desired state");
        }
    }
    @Override
    public List<TrainerTrainingInfoResponse> getTrainerTrainingsByCriteria(TrainerTrainingsRequest request) {
        List<Training> trainings = trainerRepository.getTrainerTrainingsByCriteria(request.username(),request.periodFrom(),request.periodTo(),request.traineeName());
        log.info("Retrieved Trainer Trainings by Criteria: {}, Trainings: {}",request, trainings);
        return trainingDTOMapper.mapTrainerTrainingsToDto(trainings);
    }
}
