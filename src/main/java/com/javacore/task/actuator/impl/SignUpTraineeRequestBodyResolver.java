package com.javacore.task.actuator.impl;
import com.javacore.task.actuator.RequestBodyResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SignUpTraineeRequestBodyResolver implements RequestBodyResolver {

    @Override
    public String getRequestBody() {
        log.info("Returning trainee sign up request body");
        return """
            {
                       "firstName":"Kairat",
                       "lastName": "Uzenov1",
                       "dateOfBirth":"2004-10-23",
                       "address" :"Patrice Lumumba"
            }
            """;
    }
}
