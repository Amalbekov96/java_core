package com.javacore.task.services;

import com.javacore.task.models.TraineeModel;

import java.util.List;

public interface TraineeService {
    TraineeModel getTraineeById(Long traineeId);

    TraineeModel createTrainee(TraineeModel traineeModel);

    TraineeModel updateTrainee(Long traineeId, TraineeModel traineeModel);

    void deleteTrainee(Long traineeId);

    List<TraineeModel> findAll();
}