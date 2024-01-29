package com.javacore.task.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;


public record TraineeRequest(
        @NotBlank(message = "First name cannot be null")
        String firstName,
        @NotBlank(message = "Last name cannot be null")
        String lastName,
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date dateOfBirth,
        String address
) {

}
