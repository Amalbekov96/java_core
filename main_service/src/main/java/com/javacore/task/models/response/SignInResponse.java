package com.javacore.task.models.response;

import com.javacore.task.enums.UserRole;
import lombok.Builder;

@Builder
public record SignInResponse(
        String token,
        String username,
        UserRole role
) {
}
