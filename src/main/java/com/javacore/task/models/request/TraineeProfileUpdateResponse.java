package com.javacore.task.models.request;

import com.javacore.task.models.response.TrainersListResponse;
import lombok.*;

import java.util.Date;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraineeProfileUpdateResponse {
    private String username;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private boolean isActive;
    private List<TrainersListResponse> trainers;
}
