package com.javacore.task.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record TraineeUpdateRequest(
        @NotBlank(message = "User name cannot be blank")
        String userName,
        @NotBlank(message = "First name cannot be blank")
        String firstName,
        @NotBlank(message = "Last name cannot be blank")
        String lastName,
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date dateOfBirth,
        String address,
        @NotNull(message = "Active status cannot be null")
        Boolean isActive
) {
}
