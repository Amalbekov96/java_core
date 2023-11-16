package com.javacore.task.services;

import com.javacore.task.models.TrainerModel;

public interface TrainerService {
    TrainerModel getTrainerById(Long trainerId);

    TrainerModel createTrainer(TrainerModel trainerModel);

    TrainerModel updateTrainer(Long trainerId, TrainerModel trainerModel);

    void deleteTrainer(Long trainerId);
}
