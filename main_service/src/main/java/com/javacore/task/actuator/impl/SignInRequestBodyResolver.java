package com.javacore.task.actuator.impl;

import com.javacore.task.actuator.RequestBodyResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SignInRequestBodyResolver implements RequestBodyResolver {

    @Override
    public String getRequestBody() {
        log.info("Returning request body for sign in");
        return """
                    {
                        "username": "Kushtar.Amalbekov",
                        "password": "Ajj9FlJhX6"
                    }
                    """;
    }
}
