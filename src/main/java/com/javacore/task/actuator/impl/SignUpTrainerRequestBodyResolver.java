package com.javacore.task.actuator.impl;

import com.javacore.task.actuator.RequestBodyResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SignUpTrainerRequestBodyResolver implements RequestBodyResolver {

    @Override
    public String getRequestBody() {
        log.info("Returning trainer sign up request body");
        return """
                    {
                        "firstName": "Jaina",
                        "lastName": "Kadyralieva",
                        "specialization": {
                            "id": 1,
                            "trainingTypes": "FITNESS"
                        }
                    }
                    """;
    }
}
