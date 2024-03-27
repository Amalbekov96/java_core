package com.javacore.task.actuator.impl;

import com.javacore.task.actuator.RequestBodyResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainerTrainingsRequestBodyResolver implements RequestBodyResolver {

    @Override
    public String getRequestBody() {
        log.info("Returning request body for trainer trainings");
        return """
                {
                    "username": "Kushtar.Amalbekov",
                    "periodFrom": "2023-08-30",
                    "periodTo": "2024-03-01",
                    "traineeName": "Kanysh.Abdyrakmanova"
                }
                """;
    }
}
