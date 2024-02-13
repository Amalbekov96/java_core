package com.javacore.task.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NotAssignedTrainersListResponse {
    private String userName;
    private String firstName;
    private String lastName;
    private String trainingTypes;
}
