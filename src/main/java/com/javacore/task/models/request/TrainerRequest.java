package com.javacore.task.models.request;

import com.javacore.task.entities.TrainingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRequest {

        @NotBlank(message = "First name cannot be null or empty")
        private String firstName;

        @NotBlank(message = "Last name cannot be null or empty")
        private String lastName;

        @NotNull(message = "Specialization cannot be null or empty")
        private TrainingType specialization;
}