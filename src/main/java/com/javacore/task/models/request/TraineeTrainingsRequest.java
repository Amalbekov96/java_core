package com.javacore.task.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record TraineeTrainingsRequest(
        String traineeUsername,
        String trainerName,
        String trainingType,
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date periodFrom,
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date periodTo
) {
}
