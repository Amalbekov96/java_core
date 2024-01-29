package com.javacore.task.services;

import com.javacore.task.models.*;
import com.javacore.task.models.request.TraineeTrainingsRequest;
import com.javacore.task.models.response.TraineeInfoResponse;
import com.javacore.task.models.response.TrainersListResponse;
import com.javacore.task.models.response.TrainingInfoResponse;

import java.util.List;

public interface TraineeService {
    TraineeModel getTraineeById(Long traineeId);

    TraineeModel createTrainee(TraineeModel traineeModel);

    TraineeModel updateTrainee(Long traineeId, TraineeModel traineeModel);

    void deleteTrainee(Long traineeId);
    TraineeInfoResponse findTraineeProfileByUsername(String username);
    void changeTraineePassword(long id,String password, String newPassword);
    String updateTraineeStatus(boolean status, long traineeId);
    List<TrainersListResponse> updateTraineeTrainersList(String username, List<String> trainersUsernames);
    List<TrainersListResponse> getNotAssignedActiveTrainersListForTrainee(String username);
    List<TrainingInfoResponse> getTraineeTrainingsByCriteria(TraineeTrainingsRequest request);

}