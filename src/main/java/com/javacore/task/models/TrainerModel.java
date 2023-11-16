package com.javacore.task.models;

import lombok.Data;

@Data
public class TrainerModel {
    private Long id;
    private TrainingTypeModel specialization;
    private UserModel user;
}
