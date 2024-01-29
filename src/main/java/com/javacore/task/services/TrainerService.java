package com.javacore.task.services;

import com.javacore.task.models.response.TrainerInfoResponse;
import com.javacore.task.models.TrainerModel;
import com.javacore.task.models.request.TrainerTrainingsRequest;
import com.javacore.task.models.response.TrainingInfoResponse;

import java.util.List;

public interface TrainerService {
    TrainerModel getTrainerById(Long trainerId);

    TrainerModel createTrainer(TrainerModel trainerModel);

    TrainerModel updateTrainer(Long trainerId, TrainerModel trainerModel);

    void deleteTrainer(Long trainerId);
    TrainerInfoResponse findTrainerProfileByUsername(String username);
    void changeTrainerPassword(long id,String password, String newPassword);
    String updateTrainerStatus(boolean status, long trainerId);
    List<TrainingInfoResponse> getTrainerTrainingsByCriteria(TrainerTrainingsRequest request);

}
