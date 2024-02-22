package com.javacore.task.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record TrainerTrainingsRequest(
        @NotBlank(message = "User name cannot be null")
        String username,
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date periodFrom,
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date periodTo,
        String traineeName
) {
}
