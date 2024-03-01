package com.javacore.task.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainingsRequest {
        @NotBlank(message = "User name cannot be null")
        private String traineeName;

        private String trainerName;

        private String trainingType;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date periodFrom;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date periodTo;
}