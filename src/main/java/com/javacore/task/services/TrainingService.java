package com.javacore.task.services;

import com.javacore.task.models.TrainingModel;
import com.javacore.task.models.request.TrainingRequest;

import java.io.IOException;

public interface TrainingService {
    TrainingModel getTrainingById(Long trainingId);
    TrainingModel updateTraining(Long trainingId, TrainingModel trainingModel);

    void deleteTraining(Long trainingId);
    void saveTraining(TrainingRequest training) throws IOException;
}
