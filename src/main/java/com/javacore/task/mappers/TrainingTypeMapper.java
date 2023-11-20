package com.javacore.task.mappers;

import com.javacore.task.entities.TrainingType;
import com.javacore.task.models.TrainingTypeModel;
import org.springframework.stereotype.Component;

@Component
public class TrainingTypeMapper {

    public TrainingTypeModel trainingTypeToTrainingTypeModel(TrainingType trainingType) {
        TrainingTypeModel trainingTypeModel = new TrainingTypeModel();
        trainingTypeModel.setId(trainingType.getId());
        trainingTypeModel.setTrainingTypeName(trainingType.getTrainingTypeName());
        return trainingTypeModel;
    }

    public TrainingType trainingTypeModelToTrainingType(TrainingTypeModel trainingTypeModel) {
        TrainingType trainingType = new TrainingType();
        trainingType.setId(trainingTypeModel.getId());
        trainingType.setTrainingTypeName(trainingTypeModel.getTrainingTypeName());
        return trainingType;
    }
}