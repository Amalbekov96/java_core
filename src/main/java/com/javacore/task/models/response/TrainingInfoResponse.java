package com.javacore.task.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingInfoResponse {
    private String trainingName;
    private Date trainingDate;
    private String trainingTypes;
    private Number duration;
    private String traineeName;
    private String trainerName;
}
