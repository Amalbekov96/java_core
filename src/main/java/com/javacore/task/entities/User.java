package com.javacore.task.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Boolean isActive;
}