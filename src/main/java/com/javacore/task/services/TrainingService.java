package com.javacore.task.services;

import com.javacore.task.models.TrainingModel;

public interface TrainingService {
    TrainingModel getTrainingById(Long trainingId);

    TrainingModel createTraining(TrainingModel trainingModel);

    TrainingModel updateTraining(Long trainingId, TrainingModel trainingModel);

    void deleteTraining(Long trainingId);
}
