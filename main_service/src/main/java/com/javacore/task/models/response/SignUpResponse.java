package com.javacore.task.models.response;

import com.javacore.task.enums.UserRole;
import lombok.Builder;

@Builder
public record SignUpResponse(
    String token,
    String username,
    String generatedPassword,
    UserRole role
){

}
