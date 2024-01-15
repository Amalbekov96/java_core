package com.javacore.task.services.impl;

import com.javacore.task.entities.Trainer;
import com.javacore.task.enums.ErrorCode;
import com.javacore.task.exceptions.ApiException;
import com.javacore.task.exceptions.StorageException;
import com.javacore.task.mappers.TrainerMapper;
import com.javacore.task.models.TrainerModel;
import com.javacore.task.repositories.TrainerRepository;
import com.javacore.task.services.TrainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;

    @Override
    public TrainerModel getTrainerById(Long trainerId) {
        Trainer trainer = trainerRepository.findById(trainerId);
        if(trainer != null) {
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
            Trainer existingTrainer = trainerRepository.findById(trainerId);
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
            trainerRepository.deleteById(trainerId);
        } catch (Exception e) {
            log.error("Error deleting Trainer", e);
            throw new ApiException("Error deleting Trainer", ErrorCode.TRAINER_NOT_FOUND);
        }
    }
}
