package com.javacore.task.mappers;

import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.TrainingType;
import com.javacore.task.entities.User;
import com.javacore.task.models.TrainerModel;
import com.javacore.task.models.TrainingTypeModel;
import com.javacore.task.models.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainerMapper {

    private final TrainingTypeMapper trainingTypeMapper;

    private final UserMapper userMapper;

    public TrainerModel trainerToTrainerModel(Trainer trainer) {
        TrainerModel trainerModel = new TrainerModel();
        trainerModel.setId(trainer.getId());

        // Map TrainingType using TrainingTypeMapper
        if (trainer.getSpecialization() != null) {
            TrainingTypeModel trainingTypeModel = trainingTypeMapper.trainingTypeToTrainingTypeModel(trainer.getSpecialization());
            trainerModel.setSpecialization(trainingTypeModel);
        }

        // Map User using UserMapper
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
}