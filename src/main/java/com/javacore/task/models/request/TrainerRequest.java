package com.javacore.task.models.request;

import com.javacore.task.entities.TrainingType;
import jakarta.validation.constraints.NotBlank;

public record TrainerRequest(
        @NotBlank(message = "First name cannot be null")
        String firstName,
        @NotBlank(message = "Last name cannot be null")
        String lastName,
        @NotBlank(message = "Specialization cannot be null")
        TrainingType specialization
) {
}
