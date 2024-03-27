package com.javacore.task.actuator.impl;

import com.javacore.task.actuator.RequestBodyResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateTrainingRequestBodyResolver implements RequestBodyResolver {

    @Override
    public String getRequestBody() {
        log.info("Returning request body for creating training");
        return """
                {
                    "traineeUsername" : "Kanysh.Abdyrakmanova",
                    "trainerUsername": "Kushtar.Amalbekov",
                    "trainingName": "Training 1",
                    "trainingDate" : "2024-02-28",
                    "duration": 7
                }
                """;
    }
}
