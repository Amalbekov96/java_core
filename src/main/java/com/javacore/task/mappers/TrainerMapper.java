package com.javacore.task.mappers;

import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.TrainingType;
import com.javacore.task.entities.User;
import com.javacore.task.models.TrainerModel;
import com.javacore.task.models.TrainingTypeModel;
import com.javacore.task.models.UserModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TrainerMapper {
    TrainerModel trainerToTrainerModel(Trainer trainer);

    Trainer trainerModelToTrainer(TrainerModel trainerModel);

    void update(TrainerModel trainerModel, @MappingTarget Trainer trainer);

}