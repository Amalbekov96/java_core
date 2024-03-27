package com.javacore.task.models;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TrainingModel {
    private Long id;
    private TraineeModel trainee;
    private TrainerModel trainer;
    private String trainingName;
    private Date trainingDate;
    private Number trainingDuration;
    TrainingTypeModel trainingTypeModel;
}