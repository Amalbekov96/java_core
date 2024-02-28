package com.javacore.task.services;

import com.javacore.task.models.*;
import com.javacore.task.models.response.TraineeProfileUpdateResponse;
import com.javacore.task.models.request.TraineeTrainingsRequest;
import com.javacore.task.models.request.TraineeUpdateRequest;
import com.javacore.task.models.response.TraineeInfoResponse;
import com.javacore.task.models.response.TrainersListResponse;
import com.javacore.task.models.response.TraineeTrainingInfoResponse;

import java.util.List;

public interface TraineeService {
    TraineeModel getTraineeById(Long traineeId);
    TraineeProfileUpdateResponse updateTrainee(TraineeUpdateRequest request);
    void deleteTrainee(String username);
    TraineeInfoResponse findTraineeProfileByUsername(String username);
    String updateTraineeStatus(boolean status, String username);
    List<TrainersListResponse> updateTraineeTrainersList(String username, List<String> trainersUsernames);
    List<TrainersListResponse> getNotAssignedActiveTrainersListForTrainee(String username);
    List<TraineeTrainingInfoResponse> getTraineeTrainingsByCriteria(TraineeTrainingsRequest request);

}