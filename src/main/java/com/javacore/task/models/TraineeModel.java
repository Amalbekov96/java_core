package com.javacore.task.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeModel {
    private Long traineeId;
    private Date dateOfBirth;
    private String address;
    private UserModel user;
}