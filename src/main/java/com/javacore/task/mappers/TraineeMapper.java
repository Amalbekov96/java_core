package com.javacore.task.mappers;

import com.javacore.task.entities.Trainee;
import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.User;
import com.javacore.task.models.response.TraineeInfoResponse;
import com.javacore.task.models.TraineeModel;
import com.javacore.task.models.response.TrainersListResponse;
import com.javacore.task.models.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
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
        }
    }

    public List<TrainersListResponse> mapTraineesTrainersToDto(List<Trainer> trainers) {
        return trainers.stream()
                .map(this::mapTrainerToDto)
                .collect(Collectors.toList());
    }

    private TrainersListResponse mapTrainerToDto(Trainer trainer) {
        return new TrainersListResponse(
                trainer.getUser().getUsername(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getSpecialization() != null ? trainer.getSpecialization().getTrainingType().name() : null
        );
    }
    public TraineeInfoResponse traineeInfoResponse(Trainee trainee) {
        return new TraineeInfoResponse(
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.getUser().getIsActive(),
                trainee.getTrainers().stream()
                        .map(trainer -> new TrainersListResponse(
                                trainer.getUser().getUsername(),
                                trainer.getUser().getFirstName(),
                                trainer.getUser().getLastName(),
                                trainer.getSpecialization().getTrainingType().name()
                        ))
                        .collect(Collectors.toList()));
    }
}
