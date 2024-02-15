package com.javacore.task.controllers;

import com.javacore.task.models.request.SignInRequest;
import com.javacore.task.models.request.TrainerTrainingsRequest;
import com.javacore.task.models.request.TrainerUpdateRequest;
import com.javacore.task.models.response.SignInResponse;
import com.javacore.task.models.response.TrainerInfoResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class TrainerControllerTest {
    private static final String BASE_URL = "http://localhost:8080";
    private SignInResponse signInResponse;

    @BeforeEach
    void setUp() {

        io.restassured.RestAssured.port = 8080;
        SignInRequest signInRequest = new SignInRequest("Kushtar.Amalbekov", "3uNjYRTMOd");

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
    void testGetTrainerById() {

        long trainerId = 2;

       given()
               .header("Authorization", "Bearer " + signInResponse.token())
               .contentType(ContentType.JSON)
                .pathParam("trainerId", trainerId)
                .when()
                .get(BASE_URL + "/trainers/{trainerId}")
                .then()
                .log().all()
                .statusCode(200)
               .extract().asString();
    }

    @Test
    void testUpdateTrainer() {
        TrainerUpdateRequest requestBody =
                new TrainerUpdateRequest("Kushtar.Amalbekov",
                        "Alanush", "Mamanovichov",false);

       TrainerInfoResponse trainerInfoResponse =  given()
               .header("Authorization", "Bearer " + signInResponse.token())
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put(BASE_URL + "/trainers")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(TrainerInfoResponse.class);

       assertThat(trainerInfoResponse.getFirstName(), equalTo("Alanush"));
       assertThat(trainerInfoResponse.getLastName(), equalTo("Mamanovichov"));

    }

    @Test
    void testGetTraineeProfile() {

        String username = "Aiperi.Adylova";

       TrainerInfoResponse trainerInfoResponse =  given()
               .header("Authorization", "Bearer " + signInResponse.token())
                .when()
                .get(BASE_URL + "/trainers?q=" + username)
                .then()
                .statusCode(200)
                .extract()
                .as(TrainerInfoResponse.class);

         assertThat(trainerInfoResponse.getFirstName(), equalTo("Aiperi"));
         assertThat(trainerInfoResponse.getLastName(), equalTo("Adylova"));


    }

    @Test
    void testUpdateTraineeStatus() {

        String username = "Aijamal.Asangazieva.2";
        boolean choice = false;

        given()
                .header("Authorization", "Bearer " + signInResponse.token())
                .when()
                .patch(BASE_URL + "/trainers?username=" + username + "&status=" + choice)
                .then()
                .statusCode(200)
                .body(equalTo("Deactivated"));
    }

    @Test
    void testGetTrainerTrainingsList(){
        TrainerTrainingsRequest requestBody = new TrainerTrainingsRequest(
                "Kushtar.Amalbekov",
                java.sql.Date.valueOf(LocalDate.of(2023, 10, 11)),
                java.sql.Date.valueOf(LocalDate.of(2024, 2, 11)),
                "Aliya.Rahman.2"
        );
        given()
                .header("Authorization", "Bearer " + signInResponse.token())
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(BASE_URL + "/trainers/trainer/trainings?username=" + "Kushtar.Amalbekov")
                .then()
                .log().all()
                .statusCode(200)
                .extract().asString();
    }
}
