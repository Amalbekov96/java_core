package com.javacore.task.mappers;

import com.javacore.task.entities.TrainingType;
import com.javacore.task.models.TrainingTypeModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TrainingTypeMapper {
    TrainingTypeModel trainingTypeToTrainingTypeModel(TrainingType trainingType);

    TrainingType trainingTypeModelToTrainingType(TrainingTypeModel trainingTypeModel);
}