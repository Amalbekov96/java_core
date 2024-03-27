package com.javacore.task.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerUpdateRequest {
        @NotBlank(message = "User name cannot be null or empty")
        private String userName;

        @NotBlank(message = "First name cannot be null or empty")
        private String firstName;

        @NotBlank(message = "Last name cannot be null or empty")
        private String lastName;

        @NotNull(message = "Active status cannot be null")
        private Boolean isActive;
}