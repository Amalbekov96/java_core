package com.javacore.task.models;


import lombok.Data;

@Data
public class UserModel {
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Boolean isActive;
}
