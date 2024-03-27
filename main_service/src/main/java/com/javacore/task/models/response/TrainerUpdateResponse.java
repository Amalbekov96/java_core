package com.javacore.task.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerUpdateResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String trainingTypes;
    private boolean isActive;
    private List<TraineesListResponse> trainees;
}
