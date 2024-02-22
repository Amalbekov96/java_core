package com.javacore.task.models.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record TrainingRequest (
    @NotBlank(message = "Trainee username cannot be null")
    String traineeUsername,
    @NotBlank(message = "Trainer username cannot be null")
    String trainerUsername,
    @NotBlank(message = "Training name cannot be null")
    String trainingName,
    @NotBlank(message = "Training date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date trainingDate,
    @NotBlank(message = "Training duration cannot be null")
    Number duration
) {

}
