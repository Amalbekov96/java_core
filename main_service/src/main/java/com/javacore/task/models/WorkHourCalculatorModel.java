package com.javacore.task.models;

import com.javacore.task.enums.WorkHourCalculateType;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkHourCalculatorModel {
    private Long id;
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private Boolean isActive;
    private Date trainingDate;
    private Long trainingDuration;
    private WorkHourCalculateType actionType;
}
