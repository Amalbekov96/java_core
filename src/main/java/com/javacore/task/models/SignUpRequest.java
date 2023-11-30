package com.javacore.task.models;

import com.javacore.task.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private UserRole role;
}
