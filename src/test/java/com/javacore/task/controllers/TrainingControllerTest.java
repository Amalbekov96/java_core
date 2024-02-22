package com.javacore.task.controllers;

import com.javacore.task.models.request.SignInRequest;
import com.javacore.task.models.request.TrainingRequest;
import com.javacore.task.models.response.SignInResponse;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
@Transactional
class TrainingControllerTest {
    private static final String BASE_URL = "http://localhost:8080";
    private SignInResponse signInResponse;

    @BeforeEach
    void setUp() {
        io.restassured.RestAssured.port = 8080;
        SignInRequest signInRequest = new SignInRequest("Kushtar.Amalbekov", "ziJ4jlTA22");

        signInResponse = given()
                .contentType(ContentType.JSON)
                .body(signInRequest)
                .when()
                .post(BASE_URL+"/api/v1/auth/sign-in")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(SignInResponse.class);

    }
    @Test
    void testCreateTraining() {
        TrainingRequest requestBody = new TrainingRequest(
                "Kanysh.Abdyrakmanova",
                "Kushtar.Amalbekov",
                "FITNESS",
                new Date(),
                2
        );
        given()
                .header("Authorization", "Bearer " + signInResponse.token())
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(BASE_URL + "/trainings")
                .then()
                .log().all()
                .statusCode(200)
                .body(equalTo("saved successfully"))
                .extract().asString();
    }
}
