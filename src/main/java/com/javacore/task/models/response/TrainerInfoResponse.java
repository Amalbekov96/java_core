package com.javacore.task.models.response;

import com.javacore.task.models.TraineeModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerInfoResponse {
    private String firstName;
    private String lastName;
    private String trainingTypes;
    private boolean isActive;
    private List<TraineesListResponse> trainees;
}
