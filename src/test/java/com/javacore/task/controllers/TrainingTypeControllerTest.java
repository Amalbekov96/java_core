package com.javacore.task.controllers;

import com.javacore.task.models.request.SignInRequest;
import com.javacore.task.models.response.SignInResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class TrainingTypeControllerTest {
    private static final String BASE_URL = "http://localhost:8080";
    private SignInResponse signInResponse;

    @BeforeEach
    public void setUp() {
        io.restassured.RestAssured.port = 8080;
        SignInRequest signInRequest = new SignInRequest("Kushtar.Amalbekov", "ziJ4jlTA22");

        signInResponse = given()
                .contentType(ContentType.JSON)
                .body(signInRequest)
                .when()
                .post("/api/v1/auth/sign-in")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(SignInResponse.class);

    }
    @Test
    public void testGetTrainingTypes() {
        given()
                .header("Authorization", "Bearer " + signInResponse.token())
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_URL + "/training-type")
                .then()
                .log().all()
                .statusCode(200)
                .extract().asString();

    }
}