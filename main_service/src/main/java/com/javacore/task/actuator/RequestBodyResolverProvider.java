package com.javacore.task.actuator;

import com.javacore.task.actuator.impl.*;
import org.springframework.stereotype.Component;

@Component
public class RequestBodyResolverProvider {
    public static RequestBodyResolver getResolver(HealthEndpoint.EndpointType type) {

        return switch (type) {
            case SIGN_UP_TRAINEE -> new SignUpTraineeRequestBodyResolver();
            case SIGN_UP_TRAINER -> new SignUpTrainerRequestBodyResolver();
            case TRAINERS_TRAININGS_LIST -> new TrainerTrainingsRequestBodyResolver();
            case SIGN_IN -> new SignInRequestBodyResolver();
            case TRAINING_TYPE -> new TrainingTypeRequestBodyResolver();
            case TRAINING -> new CreateTrainingRequestBodyResolver();
            case UPDATE_TRAINEE -> new UpdateTraineeByIdRequestBodyResolver();
            case UPDATE_TRAINER -> new UpdateTrainerByIdRequestBodyResolver();
            case TRAINEE_TRAININGS_LIST -> new TraineeTrainingsListRequestBodyResolver();
            case UPDATE_TRAINERS_LIST -> new UpdateTrainersRequestBodyResolver();
            case CHANGE_PASSWORD -> new UpdatePasswordRequestBodyResolver();
            default -> null;
        };
    }
}
