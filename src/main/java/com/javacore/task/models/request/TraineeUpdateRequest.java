package com.javacore.task.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeUpdateRequest {
        @NotBlank(message = "User name cannot be blank")
        private String userName;

        @NotBlank(message = "First name cannot be blank")
        private String firstName;

        @NotBlank(message = "Last name cannot be blank")
        private String lastName;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date dateOfBirth;

        private String address;

        @NotNull(message = "Active status cannot be null")
        private Boolean isActive;
}