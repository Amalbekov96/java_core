package com.javacore.task.models.response;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraineeTrainingInfoResponse {
    private String trainingName;
    private Date trainingDate;
    private String trainingTypes;
    private Number duration;
    private String trainerName;
}
