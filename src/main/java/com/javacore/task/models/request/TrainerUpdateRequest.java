package com.javacore.task.models.request;

import jakarta.validation.constraints.NotBlank;

public record TrainerUpdateRequest(
        @NotBlank(message = "User name cannot be null")
        String userName,
        @NotBlank(message = "First name cannot be null")
        String firstName,
        @NotBlank(message = "Last name cannot be null")
        String lastName,
        @NotBlank
        boolean isActive
) {
}
