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

public class TrainerControllerTest {
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
    public void testGetTrainerById() {

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
    public void testUpdateTrainer() {
        TrainerUpdateRequest requestBody =
                new TrainerUpdateRequest("Aijamal.Asangazieva.2",
                        "Alanushka", "Mamanovichov",false);

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

       assertThat(trainerInfoResponse.getFirstName(), equalTo("Alanushka"));
       assertThat(trainerInfoResponse.getLastName(), equalTo("Mamanovichov"));

    }

    @Test
    public void testGetTraineeProfile() {

        String username = "Aijamal.Asangazieva.2";

       TrainerInfoResponse trainerInfoResponse =  given()
               .header("Authorization", "Bearer " + signInResponse.token())
                .when()
                .get(BASE_URL + "/trainers?q=" + username)
                .then()
                .statusCode(200)
                .extract()
                .as(TrainerInfoResponse.class);

         assertThat(trainerInfoResponse.getFirstName(), equalTo("Aijamal"));
         assertThat(trainerInfoResponse.getLastName(), equalTo("Asangazieva"));


    }

    @Test
    public void testUpdateTraineeStatus() {

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
    public void testGetTrainerTrainingsList(){
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