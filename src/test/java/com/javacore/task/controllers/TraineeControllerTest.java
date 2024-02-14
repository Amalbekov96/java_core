package com.javacore.task.controllers;

import com.javacore.task.models.request.*;
import com.javacore.task.models.response.SignInResponse;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
@Transactional
public class TraineeControllerTest {
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
    public void testDeleteTrainee() {

        String username = "Eldiyar.Toktomamatov";

        given()
                .header("Authorization", "Bearer " + signInResponse.token())
                .when()
                .delete(BASE_URL + "/trainees?username=" + username)
                .then()
                .statusCode(200)
                .body(equalTo("deleted successfully"));
    }


    @Test
    public void testUpdateTrainee() {

        TraineeUpdateRequest requestBody =
                new TraineeUpdateRequest("John.Doe",
                        "Alanushka", "Mamanovichov",
                        new Date(),
                        "123 Main St",true);

       TraineeProfileUpdateResponse traineeProfileUpdateResponse =  given()
               .header("Authorization", "Bearer " + signInResponse.token())
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put(BASE_URL + "/trainees" )
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(TraineeProfileUpdateResponse.class);

        assertThat(traineeProfileUpdateResponse.getFirstName(), equalTo(requestBody.firstName()));
        assertThat(traineeProfileUpdateResponse.getLastName(), equalTo(requestBody.lastName()));
        assertThat(traineeProfileUpdateResponse.getAddress(), equalTo(requestBody.address()));
        assertThat(traineeProfileUpdateResponse.getDateOfBirth(), equalTo(requestBody.dateOfBirth()));


    }

    @Test
    public void testGetTraineeProfile() {

        String username = "John.Doe";

        TraineeInfoResponse response = given()
                .header("Authorization", "Bearer " + signInResponse.token())
                .when()
                .get(BASE_URL + "/trainees?q=" + username)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(TraineeInfoResponse.class);

        assertThat(response.getFirstName(), notNullValue());
        assertThat(response.getLastName(), notNullValue());
        assertThat(response.getAddress(), notNullValue());
    }
    @Test
    public void testUpdateTraineeStatus() {
        boolean choice = false;
       String username = "John.Doe";

        given()
                .header("Authorization", "Bearer " + signInResponse.token())
                .when()
                .patch(BASE_URL + "/trainees?username=" + username + "&status=" + choice)
                .then()
                .log().all()
                .statusCode(200)
                .body(equalTo("Deactivated"));
    }


    @Test
    public void testGetNotAssignedTrainersForTrainee() {

        String username = "John.Doe";

        given()
                .header("Authorization", "Bearer " + signInResponse.token())
                .when()
                .get(BASE_URL + "/trainees/not-assigned-trainers?username=" + username)
                .then()
                .log().all()
                .statusCode(200)
                .extract().asString();
    }

    @Test
    public void testUpdateTraineeTrainersList() {
        String username = "John.Doe";
        List<String> trainersUsernames = Arrays.asList("Kushtar.Amalbekov","Eulan.Ibraimov");

        given()
                .header("Authorization", "Bearer " + signInResponse.token() )
                .contentType(ContentType.JSON)
                .body(trainersUsernames)
                .when()
                .put(BASE_URL + "/trainees/update-trainers?username=" + username)
                .then()
                .log().all()
                .statusCode(200)
                .extract().asString();

    }

    @Test
    public void testGetTraineeTrainingsList() {
        String username = "Kushtar.Amalbekov_2";
        TraineeTrainingsRequest requestBody = new TraineeTrainingsRequest(
                username,
                "Kushtar.Amalbekov",
                "Fitness",
                java.sql.Date.valueOf(LocalDate.of(2021, 10, 11)),
                java.sql.Date.valueOf(LocalDate.of(2024, 2, 11))
        );
        given()
                .header("Authorization", "Bearer " + signInResponse.token())
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .get(BASE_URL + "/trainees/trainee/trainings?username=" + username)
                .then()
                .log().all()
                .statusCode(200)
                .extract().asString();

    }
}
