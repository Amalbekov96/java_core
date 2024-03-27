package com.javacore.task.actuator.impl;

import com.javacore.task.actuator.RequestBodyResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TraineeTrainingsListRequestBodyResolver implements RequestBodyResolver {

   @Override
    public String getRequestBody() {
        log.info("Returning request body for trainee trainings list");
        return """
                {
                    "traineeName" :"Kanysh.Abdyrakmanova",
                    "trainerName": "Kushtar.Amalbekov",
                    "trainingType": "FITNESS",
                    "periodFrom": "2023-12-09",
                    "periodTo": "2024-03-28"
                }
                """;
    }
}
