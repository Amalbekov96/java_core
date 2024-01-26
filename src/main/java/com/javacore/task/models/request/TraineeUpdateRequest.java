package com.javacore.task.models.request;

import java.time.LocalDate;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;

public record TraineeUpdateRequest (
    @NotBlank(message = "User name cannot be null")
    String userName,
    @NotBlank(message = "First name cannot be null")
    String firstName,
    @NotBlank(message = "Last name cannot be null")
    String lastName,
    Date dateOfBirth,
    String address,
    @NotBlank
    boolean isActive
) {
}
