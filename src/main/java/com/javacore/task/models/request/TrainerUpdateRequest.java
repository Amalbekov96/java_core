package com.javacore.task.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TrainerUpdateRequest(
        @NotBlank(message = "User name cannot be blank")
        String userName,
        @NotBlank(message = "First name cannot be blank")
        String firstName,
        @NotBlank(message = "Last name cannot be blank")
        String lastName,
        @NotNull(message = "Active status cannot be null")
        Boolean isActive
) {
}
