package com.javacore.task.mappers;

import com.javacore.task.entities.Trainee;
import com.javacore.task.entities.User;
import com.javacore.task.models.TraineeModel;
import com.javacore.task.models.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TraineeMapper {

    private final UserMapper userMapper;

    public TraineeModel traineeToTraineeModel(Trainee trainee) {
        TraineeModel traineeModel = new TraineeModel();
        traineeModel.setTraineeId(trainee.getTraineeId());
        traineeModel.setDateOfBirth(trainee.getDateOfBirth());
        traineeModel.setAddress(trainee.getAddress());

        // Map User details using UserMapper
        if (trainee.getUser() != null) {
            User user = trainee.getUser();
            UserModel userModel = userMapper.userToUserModel(user);
            traineeModel.setUser(userModel);
        }

        return traineeModel;
    }

    public Trainee traineeModelToTrainee(TraineeModel traineeModel) {
        Trainee trainee = new Trainee();
        trainee.setTraineeId(traineeModel.getTraineeId());
        trainee.setDateOfBirth(traineeModel.getDateOfBirth());
        trainee.setAddress(traineeModel.getAddress());

        // Map User details using UserMapper
        if (traineeModel.getUser() != null) {
            UserModel userModel = traineeModel.getUser();
            User user = userMapper.userModelToUser(userModel);
            trainee.setUser(user);
        }

        return trainee;
    }

    public void update(TraineeModel traineeModel, Trainee trainee) {
        if (traineeModel != null && trainee != null) {
            trainee.setAddress(traineeModel.getAddress());
            trainee.setDateOfBirth(traineeModel.getDateOfBirth());
            trainee.setTraineeId(traineeModel.getTraineeId());
            trainee.setUser(userMapper.userModelToUser(traineeModel.getUser()));
            // Update other fields as needed
        }
    }

    public List<TraineeModel> traineesToTraineeModels(List<Trainee> trainees) {
        return trainees.stream()
                .map(this::traineeToTraineeModel)
                .collect(Collectors.toList());
    }

    public List<Trainee> traineeModelsToTrainees(List<TraineeModel> traineeModels) {
        return traineeModels.stream()
                .map(this::traineeModelToTrainee)
                .collect(Collectors.toList());
    }
}
