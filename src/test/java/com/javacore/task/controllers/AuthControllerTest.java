package com.javacore.task.controllers;

import com.javacore.task.entities.TrainingType;
import com.javacore.task.entities.User;
import com.javacore.task.enums.TrainingTypes;
import com.javacore.task.enums.UserRole;
import com.javacore.task.models.request.SignInRequest;
import com.javacore.task.models.request.TraineeRequest;
import com.javacore.task.models.request.TrainerRequest;
import com.javacore.task.models.response.SignInResponse;
import com.javacore.task.models.response.SignUpResponse;
import com.javacore.task.repositories.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
public class AuthControllerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @BeforeEach
    public void setUp() {
        RestAssured.port = 8080;
    }

    @Test
    public void testTraineeSignUp() {
        TraineeRequest traineeRequest = new TraineeRequest("John",
                "Doe",new Date(), "123 Main St");

        SignUpResponse sign = given()
                .contentType(ContentType.JSON)
                .body(traineeRequest)
                .when()
                .post("/api/v1/auth/trainee/sign-up")
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
    public void testTrainerSignUp() {
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
                .post("/api/v1/auth/trainer/sign-up")
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
    public void testSignIn() {
        SignInRequest signInRequest = new SignInRequest("Kushtar.Amalbekov", "ziJ4jlTA22");

        SignInResponse signInResponse = given()
                .contentType(ContentType.JSON)
                .body(signInRequest)
                .when()
                .post("/api/v1/auth/sign-in")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(SignInResponse.class);

        assertThat(signInResponse.token(), notNullValue());
        assertThat(signInResponse.role(), notNullValue());
        assertThat(signInResponse.username(), notNullValue());
    }

    @Test
    public void testUpdateLogin() {
        String username = "Kushtar.Amalbekov";
        String password = "ziJ4jlTA22";
        String newPassword = "newPassword";

        // Authenticate the user and obtain the token
        String token = given()
                .contentType(ContentType.JSON)
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .post("/api/v1/auth/sign-in")
                .then()
                .extract()
                .path("token");

        // Make the update request with the obtained token
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .queryParam("username", username)
                .queryParam("password", password)
                .queryParam("newPassword", newPassword)
                .when()
                .put("/api/v1/auth/update-login")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.TEXT) // Expect the content type to be plain text
                .body(equalTo("password successfully updated!"));

        User updatedUser = userRepository.findUserByUsername(username).orElse(null);
        Assertions.assertNotNull(updatedUser);
        Assertions.assertTrue(passwordEncoder.matches(newPassword, updatedUser.getPassword()));
    }



}