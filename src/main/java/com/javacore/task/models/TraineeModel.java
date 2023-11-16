package com.javacore.task.models;

import lombok.Data;

import java.util.Date;

@Data
public class TraineeModel {
    private Long traineeId;
    private Date dateOfBirth;
    private String address;
    private UserModel user;
}