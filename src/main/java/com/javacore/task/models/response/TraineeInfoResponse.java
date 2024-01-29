package com.javacore.task.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeInfoResponse {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private boolean isActive;
    private List<TrainersListResponse> trainers;
}
