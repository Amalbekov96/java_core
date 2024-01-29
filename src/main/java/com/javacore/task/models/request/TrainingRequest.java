package com.javacore.task.models.request;


import java.util.Date;

public record TrainingRequest (
    String traineeUsername,
    String trainerUsername,
    String trainingName,
    Date trainingDate,
    int duration
) {

}
