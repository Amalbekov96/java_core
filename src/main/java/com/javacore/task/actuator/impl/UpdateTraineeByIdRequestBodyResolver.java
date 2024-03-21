package com.javacore.task.actuator.impl;

import com.javacore.task.actuator.RequestBodyResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdateTraineeByIdRequestBodyResolver implements RequestBodyResolver {

    @Override
    public String getRequestBody() {
        log.info("Returning request body for update trainee by id");
        return """
            {
                "userName": "Eldiyar.Toktomamatov",
                "firstName": "aaaa",
                "lastName": "Toktomamatov",
                "dateOfBirth": "2001-09-08",
                "address": "Tokmok",
                "isActive": true
            }
            """;
    }
}
