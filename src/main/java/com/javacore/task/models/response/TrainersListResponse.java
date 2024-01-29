package com.javacore.task.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TrainersListResponse {
    private String userName;
    private String firstName;
    private String lastName;
    private String trainingTypes;
}
