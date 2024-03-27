package com.javacore.task.actuator.impl;

import com.javacore.task.actuator.RequestBodyResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdateTrainersRequestBodyResolver implements RequestBodyResolver {

    @Override
    public String getRequestBody() {
        log.info("Returning request body for updating trainers");
        return """
                    [
                        "Jaina.Kadyralieva",
                        "Aiperi.Adylova"
                    ]
                    """;
    }
}
