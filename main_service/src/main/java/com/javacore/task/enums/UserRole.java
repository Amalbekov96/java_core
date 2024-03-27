package com.javacore.task.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum UserRole {
    TRAINER("Trainer"),
    TRAINEE("Trainee");

    private final String description;

}