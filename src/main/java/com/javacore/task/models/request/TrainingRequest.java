package com.javacore.task.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingRequest {
    @NotBlank(message = "Trainee username cannot be null or empty")
    private String traineeUsername;

    @NotBlank(message = "Trainer username cannot be null or empty")
    private String trainerUsername;

    @NotBlank(message = "Training name cannot be null or empty")
    private String trainingName;

    @NotNull(message = "Training date cannot be null or empty")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date trainingDate;

    @NotNull(message = "Training duration cannot be null")
    private Number duration;
}