package com.javacore.task.mappers;

import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.TrainingType;
import com.javacore.task.entities.User;
import com.javacore.task.exceptions.InactiveUserException;
import com.javacore.task.models.TrainerModel;
import com.javacore.task.models.TrainingTypeModel;
import com.javacore.task.models.UserModel;
import com.javacore.task.models.request.TrainerUpdateRequest;
import com.javacore.task.models.response.TraineesListResponse;
import com.javacore.task.models.response.TrainerInfoResponse;
import com.javacore.task.models.response.TrainerUpdateResponse;
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

        if (trainerModel.getSpecialization() != null) {
            TrainingType trainingType = trainingTypeMapper.trainingTypeModelToTrainingType(trainerModel.getSpecialization());
            trainer.setSpecialization(trainingType);
        }

        if (trainerModel.getUser() != null) {
            User user = userMapper.userModelToUser(trainerModel.getUser());
            trainer.setUser(user);
        }

        return trainer;
    }

    public Trainer update(TrainerUpdateRequest request, Trainer trainer) {
        if (request != null && trainer != null) {
            trainer.getUser().setUsername(request.getUserName());
            trainer.getUser().setFirstName(request.getFirstName());
            trainer.getUser().setLastName(request.getLastName());
            Boolean isActive = request.getIsActive();
            if (isActive == null) {
                throw new InactiveUserException("Active status cannot be null");
            } else {
                trainer.getUser().setIsActive(request.getIsActive());
            }

        }
        return trainer;
    }
    public TrainerInfoResponse trainerInfoResponse(Trainer trainer){

        return new TrainerInfoResponse(
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getSpecialization().getTrainingType().name(),
                trainer.getUser().getIsActive(),
                trainer.getTrainees().stream().map(trainee -> new TraineesListResponse(
                        trainee.getUser().getUsername(),
                        trainee.getUser().getFirstName(),
                        trainee.getUser().getLastName()
                )).toList());
    }
    public TrainerUpdateResponse trainerToTrainerUpdateResponse(Trainer trainer) {
        return TrainerUpdateResponse.builder()
                .username(trainer.getUser().getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .trainingTypes(trainer.getSpecialization().getTrainingType().name())
                .isActive(trainer.getUser().getIsActive())
                .trainees(trainer.getTrainees().stream().map(
                        trainee -> new TraineesListResponse(
                                trainee.getUser().getUsername(),
                                trainee.getUser().getFirstName(),
                                trainee.getUser().getLastName()
                )).toList())
                .build();

    }
}