package com.javacore.task.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainee {

    private Long traineeId;
    private Date dateOfBirth;
    private String address;
    private User user;
}