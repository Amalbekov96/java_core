package com.javacore.task.services;

import com.javacore.task.models.request.TrainerUpdateRequest;
import com.javacore.task.models.response.TrainerInfoResponse;
import com.javacore.task.models.TrainerModel;
import com.javacore.task.models.request.TrainerTrainingsRequest;
import com.javacore.task.models.response.TrainerTrainingInfoResponse;
import com.javacore.task.models.response.TrainerUpdateResponse;

import java.util.List;

public interface TrainerService {
    TrainerModel getTrainerById(Long trainerId);
    TrainerUpdateResponse updateTrainer(TrainerUpdateRequest request);
    TrainerInfoResponse findTrainerProfileByUsername(String username);
    String updateTrainerStatus(boolean status, String username);
    List<TrainerTrainingInfoResponse> getTrainerTrainingsByCriteria(TrainerTrainingsRequest request);

}
