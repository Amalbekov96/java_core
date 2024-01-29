package com.javacore.task.mappers;

import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.TrainingType;
import com.javacore.task.entities.User;
import com.javacore.task.models.*;
import com.javacore.task.models.response.TrainerInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrainerMapper {

    private final TrainingTypeMapper trainingTypeMapper;

    private final UserMapper userMapper;
    private final TraineeMapper traineeMapper;

    public TrainerModel trainerToTrainerModel(Trainer trainer) {
        TrainerModel trainerModel = new TrainerModel();
        trainerModel.setId(trainer.getId());

        if (trainer.getSpecialization() != null) {
            TrainingTypeModel trainingTypeModel = trainingTypeMapper.trainingTypeToTrainingTypeModel(trainer.getSpecialization());
            trainerModel.setSpecialization(trainingTypeModel);
        }

        if (trainer.getUser() != null) {
            UserModel userModel = userMapper.userToUserModel(trainer.getUser());
            trainerModel.setUser(userModel);
        }

        return trainerModel;
    }

    public Trainer trainerModelToTrainer(TrainerModel trainerModel) {
        Trainer trainer = new Trainer();
        trainer.setId(trainerModel.getId());

        // Map TrainingType using TrainingTypeMapper
        if (trainerModel.getSpecialization() != null) {
            TrainingType trainingType = trainingTypeMapper.trainingTypeModelToTrainingType(trainerModel.getSpecialization());
            trainer.setSpecialization(trainingType);
        }

        // Map User using UserMapper
        if (trainerModel.getUser() != null) {
            User user = userMapper.userModelToUser(trainerModel.getUser());
            trainer.setUser(user);
        }

        return trainer;
    }

    public void update(TrainerModel trainerModel, Trainer trainer) {
        if (trainerModel != null && trainer != null) {
            trainer.setId(trainerModel.getId());
            trainer.setSpecialization(trainingTypeMapper.trainingTypeModelToTrainingType(trainerModel.getSpecialization()));
            trainer.setUser(userMapper.userModelToUser(trainerModel.getUser()));
        }
    }
    public TrainerInfoResponse trainerInfoResponse(Trainer trainer){

        List<TraineeModel> trainees = trainer.getTrainees().stream()
                .map(traineeMapper::traineeToTraineeModel)
                .toList();

        return new TrainerInfoResponse(
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getSpecialization().getTrainingType().name(),
                trainer.getUser().getIsActive(),
                trainees);
    }
}