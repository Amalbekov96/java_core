package com.javacore.task.models.response;
import lombok.*;

import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TraineeProfileUpdateResponse {
    private String username;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private boolean isActive;
    private List<TrainersListResponse> trainers;
}
