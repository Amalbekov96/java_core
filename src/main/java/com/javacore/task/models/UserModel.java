package com.javacore.task.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserModel {
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Boolean isActive;
}
