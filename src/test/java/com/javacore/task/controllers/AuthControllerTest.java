package com.javacore.task.controllers;

import com.javacore.task.entities.TrainingType;
import com.javacore.task.enums.TrainingTypes;
import com.javacore.task.enums.UserRole;
import com.javacore.task.models.request.SignInRequest;
import com.javacore.task.models.request.TraineeRequest;
import com.javacore.task.models.request.TrainerRequest;
import com.javacore.task.models.response.SignInResponse;
import com.javacore.task.models.response.SignUpResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
class AuthControllerTest {

    private static final String BASE_URL = "http://localhost:8080";
    @BeforeEach
    void setUp() {
        RestAssured.port = 8080;
    }

    @Test
    void testTraineeSignUp() {
        TraineeRequest traineeRequest = new TraineeRequest("John",
                "Doe",new Date(), "123 Main St");

        SignUpResponse sign = given()
                .contentType(ContentType.JSON)
                .body(traineeRequest)
                .when()
                .post(BASE_URL+"/api/v1/auth/trainee/sign-up")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(SignUpResponse.class);

        assertThat(sign.generatedPassword(), notNullValue());
        assertThat(sign.token(), notNullValue());
        assertThat(sign.username(), notNullValue());
        Assertions.assertEquals(sign.role(), UserRole.TRAINEE);
    }


    @Test
    void testTrainerSignUp() {
        TrainingType trainingType = TrainingType.builder()
                .id(2L)
                .trainingType(TrainingTypes.FITNESS)
                .build();
        TrainerRequest trainerRequest = new TrainerRequest("John",
                "Doe", trainingType);

        SignUpResponse sign = given()
                .contentType(ContentType.JSON)
                .body(trainerRequest)
                .when()
                .post(BASE_URL+"/api/v1/auth/trainer/sign-up")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(SignUpResponse.class);

        assertThat(sign.generatedPassword(), notNullValue());
        assertThat(sign.token(), notNullValue());
        assertThat(sign.role(), notNullValue());
        assertThat(sign.username(), notNullValue());
    }

    @Test
    void testSignIn() {
        SignInRequest signInRequest = new SignInRequest("Kushtar.Amalbekov", "ziJ4jlTA22");

        SignInResponse signInResponse = given()
                .contentType(ContentType.JSON)
                .body(signInRequest)
                .when()
                .post(BASE_URL+"/api/v1/auth/sign-in")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(SignInResponse.class);

        assertThat(signInResponse.token(), notNullValue());
        assertThat(signInResponse.role(), notNullValue());
        assertThat(signInResponse.username(), notNullValue());
    }
}