package com.javacore.task.mappers;

import com.javacore.task.entities.Training;
import com.javacore.task.models.TrainingModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TrainingMapper {
    @Mapping(source = "trainee", target = "trainee")
    @Mapping(source = "trainer", target = "trainer")
    TrainingModel trainingToTrainingModel(Training training);

    @Mapping(source = "trainee", target = "trainee")
    @Mapping(source = "trainer", target = "trainer")
    Training trainingModelToTraining(TrainingModel trainingModel);

    void update(TrainingModel trainingModel,@MappingTarget Training training);
}