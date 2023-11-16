package com.javacore.task.models;

import lombok.Data;

import java.util.Date;

@Data
public class TrainingModel {
    private Long id;
    private TraineeModel trainee;
    private TrainerModel trainer;
    private String trainingName;
    private Date trainingDate;
    private int trainingDuration;
}