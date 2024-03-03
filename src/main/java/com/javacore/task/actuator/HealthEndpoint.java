package com.javacore.task.actuator;

import com.javacore.task.exceptions.BadCredentialsException;
import com.javacore.task.models.response.SignInResponse;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.util.Collections;

@Component("customHealthIndicator")
@Slf4j
@Data
@Transactional
@RequiredArgsConstructor
public class HealthEndpoint implements HealthIndicator {

    private static final String apiHost= "localhost";

    private static final String apiPort = "9091";

    private final RestTemplate restTemplate;

    public static final String BASE_URL = "http://" + apiHost + ":" + apiPort;
    private static final String ENDPOINT_SIGN_UP_TRAINEE = "/api/v1/auth/trainee/sign-up";
    private static final String ENDPOINT_SIGN_UP_TRAINER = "/api/v1/auth/trainer/sign-up";
    public static final String ENDPOINT_SIGN_IN = "/api/v1/auth/sign-in";
    private static final String ENDPOINT_TRAINING = "/trainings";
    private static final String ENDPOINT_TRAINING_TYPE = "/training-type";
    private static final String ENDPOINT_GET_TRAINER_BY_NAME = "/trainers?trainerUsername=Jaina.Kadyralieva";
    private static final String ENDPOINT_CHANGE_PASSWORD = "/api/v1/auth";
    private static final String ENDPOINT_TRAINEES_BY_NAME = "/trainees?traineeUsername=Kairat.Uzenov";
    private static final String ENDPOINT_NOT_ASSIGNED_TRAINERS = "/trainees/not-assigned-trainers?username=Kairat.Uzenov";
    private static final String ENDPOINT_TRAINEE_TRAINERS_LIST = "/trainees/trainers?username=Kairat.Uzenov";
    private static final String ENDPOINT_TRAINEE_TRAININGS_LIST = "/trainees/trainings";
    private static final String ENDPOINT_TRAINER_TRAININGS_LIST = "/trainers/trainings";
    private static final String ENDPOINT_TRAINEE_BY_ID = "/trainees/3";
    private static final String ENDPOINT_DELETE_TRAINEE = "/trainees?username=Kairat.Uzenov113";
    private static final String ENDPOINT_TRAINER_BY_ID = "/trainers/1";
    private static final String ENDPOINT_UPDATE_TRAINEE = "/trainees";
    private static final String ENDPOINT_UPDATE_TRAINER = "/trainers";
    public enum EndpointType {
        SIGN_UP_TRAINEE, SIGN_UP_TRAINER, SIGN_IN, TRAINING, UPDATE_TRAINERS_LIST,TRAINEE_TRAININGS_LIST, TRAINERS_TRAININGS_LIST,
        TRAINING_TYPE, TRAINERS_BY_NAME, CHANGE_PASSWORD, TRAINEES_BY_NAME, NOT_ASSIGNED_TRAINERS,
        DELETE_TRAINEE_BY_ID, TRAINER_BY_ID, UPDATE_TRAINEE, UPDATE_TRAINER, TRAINEE_BY_ID
    }

    @Override
    public Health health() {
        for (EndpointType endpointType : EndpointType.values()) {
            String endpointUrl = getEndpointUrl(endpointType);
            boolean isEndpointHealthy = checkEndpointStatus(endpointUrl, getHttpMethod(endpointType));
            if (!isEndpointHealthy) {
                log.error("Endpoint {} is not healthy", endpointUrl);
                return Health.down().withDetail("error", "One or more endpoints are not healthy").build();
            }
        }
        return Health.up().build();
    }

    public String getEndpointUrl(EndpointType endpointType) {
        return switch (endpointType) {
            case SIGN_UP_TRAINEE -> ENDPOINT_SIGN_UP_TRAINEE;
            case SIGN_UP_TRAINER -> ENDPOINT_SIGN_UP_TRAINER;
            case SIGN_IN -> ENDPOINT_SIGN_IN;
            case TRAINING -> ENDPOINT_TRAINING;
            case TRAINING_TYPE -> ENDPOINT_TRAINING_TYPE;
            case TRAINERS_BY_NAME -> ENDPOINT_GET_TRAINER_BY_NAME;
            case CHANGE_PASSWORD -> ENDPOINT_CHANGE_PASSWORD;
            case TRAINEES_BY_NAME -> ENDPOINT_TRAINEES_BY_NAME;
            case NOT_ASSIGNED_TRAINERS -> ENDPOINT_NOT_ASSIGNED_TRAINERS;
            case UPDATE_TRAINERS_LIST -> ENDPOINT_TRAINEE_TRAINERS_LIST;
            case TRAINEE_TRAININGS_LIST -> ENDPOINT_TRAINEE_TRAININGS_LIST;
            case DELETE_TRAINEE_BY_ID -> ENDPOINT_DELETE_TRAINEE;
            case TRAINER_BY_ID -> ENDPOINT_TRAINER_BY_ID;
            case TRAINERS_TRAININGS_LIST -> ENDPOINT_TRAINER_TRAININGS_LIST;
            case UPDATE_TRAINEE -> ENDPOINT_UPDATE_TRAINEE;
            case UPDATE_TRAINER -> ENDPOINT_UPDATE_TRAINER;
            case TRAINEE_BY_ID -> ENDPOINT_TRAINEE_BY_ID;
           };
    }

    public HttpMethod getHttpMethod(EndpointType endpointType) {
        return switch (endpointType) {
            case SIGN_UP_TRAINEE, SIGN_UP_TRAINER, SIGN_IN, TRAINING ,
                    TRAINEE_TRAININGS_LIST, TRAINERS_TRAININGS_LIST -> HttpMethod.POST;
            case TRAINING_TYPE, TRAINERS_BY_NAME, TRAINEES_BY_NAME, NOT_ASSIGNED_TRAINERS, TRAINER_BY_ID, TRAINEE_BY_ID -> HttpMethod.GET;
            case  CHANGE_PASSWORD, UPDATE_TRAINERS_LIST, UPDATE_TRAINEE, UPDATE_TRAINER ->
                    HttpMethod.PUT;
            case DELETE_TRAINEE_BY_ID -> HttpMethod.DELETE;
        };
    }

    public boolean checkEndpointStatus(String endpointUrl, HttpMethod method) {
        try {
            SignInResponse signInResponse = obtainAuthToken();

            if (signInResponse == null) {
                log.error("Failed to obtain authentication token");
                return false;
            }

            var uri = URI.create(BASE_URL+endpointUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(signInResponse.token());
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = createHttpEntity(headers, endpointUrl);
            ResponseEntity<String> response = restTemplate.exchange(uri, method, entity, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Error occurred while checking endpoint status", e);
            return false;
        }
    }

    public HttpEntity<String> createHttpEntity(HttpHeaders headers, String endpointUrl) {
        RequestBodyResolver resolver = RequestBodyResolverProvider.getResolver(getEndpointType(endpointUrl));
        if (resolver == null) {
            log.warn("Resolver is null for endpoint URL: {}", endpointUrl);
            return new HttpEntity<>(headers);
        }
        String requestBody = resolver.getRequestBody();

        if (requestBody == null) {
            return new HttpEntity<>(headers);
        }
        log.info(BASE_URL + endpointUrl, apiHost, apiPort);
        return new HttpEntity<>(requestBody, headers);
    }
    public SignInResponse obtainAuthToken() {
        try {
            String signInRequestBody = "{\"username\": \"" + "John.Doe" + "\", \"password\": \"" + "123" + "\"}";
            URI signInUri = URI.create(BASE_URL + ENDPOINT_SIGN_IN);
            HttpHeaders signInHeaders = new HttpHeaders();
            signInHeaders.setContentType(MediaType.APPLICATION_JSON);
            signInHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> signInEntity = new HttpEntity<>(signInRequestBody, signInHeaders);
            ResponseEntity<SignInResponse> signInResponse = restTemplate.exchange(signInUri, HttpMethod.POST, signInEntity, SignInResponse.class);
            if (signInResponse.getBody() == null) {
                log.error("signInResponse or signInResponse body is null");
                throw new BadCredentialsException("Failed to obtain authentication token: Response is null");
            }
            return signInResponse.getBody();
        } catch (Exception e) {
            log.error("Error occurred while obtaining authentication token", e);
            throw new BadCredentialsException("Failed to obtain authentication token");
        }
    }
    private EndpointType getEndpointType(String endpointUrl) {

        return switch (endpointUrl) {
            case ENDPOINT_SIGN_UP_TRAINEE -> EndpointType.SIGN_UP_TRAINEE;
            case ENDPOINT_SIGN_UP_TRAINER -> EndpointType.SIGN_UP_TRAINER;
            case ENDPOINT_SIGN_IN -> EndpointType.SIGN_IN;
            case ENDPOINT_TRAINING -> EndpointType.TRAINING;
            case ENDPOINT_TRAINING_TYPE -> EndpointType.TRAINING_TYPE;
            case ENDPOINT_GET_TRAINER_BY_NAME -> EndpointType.TRAINERS_BY_NAME;
            case ENDPOINT_TRAINEES_BY_NAME -> EndpointType.TRAINEES_BY_NAME;
            case ENDPOINT_NOT_ASSIGNED_TRAINERS -> EndpointType.NOT_ASSIGNED_TRAINERS;
            case ENDPOINT_DELETE_TRAINEE -> EndpointType.DELETE_TRAINEE_BY_ID;
            case ENDPOINT_TRAINER_BY_ID -> EndpointType.TRAINER_BY_ID;
            case ENDPOINT_UPDATE_TRAINEE -> EndpointType.UPDATE_TRAINEE;
            case ENDPOINT_UPDATE_TRAINER -> EndpointType.UPDATE_TRAINER;
            case ENDPOINT_CHANGE_PASSWORD -> EndpointType.CHANGE_PASSWORD;
            case ENDPOINT_TRAINEE_TRAININGS_LIST -> EndpointType.TRAINEE_TRAININGS_LIST;
            case ENDPOINT_TRAINER_TRAININGS_LIST -> EndpointType.TRAINERS_TRAININGS_LIST;
            case ENDPOINT_TRAINEE_TRAINERS_LIST -> EndpointType.UPDATE_TRAINERS_LIST;
            case ENDPOINT_TRAINEE_BY_ID -> EndpointType.TRAINEE_BY_ID;
            default -> throw new IllegalStateException("Unexpected value: " + endpointUrl);
        };
    }
}