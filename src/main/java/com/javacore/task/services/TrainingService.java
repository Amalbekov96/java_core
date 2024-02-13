package com.javacore.task.services;

import com.javacore.task.models.TrainingModel;
import com.javacore.task.models.request.TrainingRequest;

import java.io.IOException;

public interface TrainingService {
    void saveTraining(TrainingRequest training) throws IOException;
}
