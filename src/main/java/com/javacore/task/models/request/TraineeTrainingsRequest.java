package com.javacore.task.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record TraineeTrainingsRequest(
        @NotBlank(message = "User name cannot be null")
        String traineeName,
        String trainerName,
        String trainingType,
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date periodFrom,
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date periodTo
) {
}
