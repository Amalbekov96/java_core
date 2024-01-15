package com.javacore.task.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Training {
    private Long id;
    private Trainee trainee;
    private Trainer trainer;
    private TrainingType trainingType;
    private String trainingName;
    private Date trainingDate;
    private int trainingDuration;
}