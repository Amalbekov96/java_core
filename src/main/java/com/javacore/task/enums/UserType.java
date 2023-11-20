package com.javacore.task.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum UserType {
    TRAINER("Trainer"),
    TRAINEE("Trainee"),
    TRAINING("Training"),
    USER("User");

    private final String description;

}