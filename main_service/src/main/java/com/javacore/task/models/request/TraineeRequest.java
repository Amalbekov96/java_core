package com.javacore.task.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeRequest {

        @NotBlank(message = "First name cannot be null or empty")
        private String firstName;

        @NotBlank(message = "Last name cannot be null or empty")
        private String lastName;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date dateOfBirth;

        private String address;
}