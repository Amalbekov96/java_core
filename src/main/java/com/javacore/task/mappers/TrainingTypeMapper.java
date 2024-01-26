package com.javacore.task.mappers;

import com.javacore.task.entities.TrainingType;
import com.javacore.task.enums.TrainingTypes;
import com.javacore.task.models.TrainingTypeModel;
import org.springframework.stereotype.Component;

@Component
public class TrainingTypeMapper {

    public TrainingTypeModel trainingTypeToTrainingTypeModel(TrainingType trainingType) {
        TrainingTypeModel trainingTypeModel = new TrainingTypeModel();
        trainingTypeModel.setId(trainingType.getId());
        trainingTypeModel.setTrainingType(trainingType.getTrainingType().name());
        return trainingTypeModel;
    }

    public TrainingType trainingTypeModelToTrainingType(TrainingTypeModel trainingTypeModel) {
        TrainingType trainingType = new TrainingType();
        trainingType.setId(trainingTypeModel.getId());
        trainingType.setTrainingType(TrainingTypes.valueOf(trainingTypeModel.getTrainingType()));
        return trainingType;
    }
}