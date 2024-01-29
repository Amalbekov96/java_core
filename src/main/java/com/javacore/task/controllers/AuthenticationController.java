package com.javacore.task.controllers;

import com.javacore.task.models.request.TraineeRequest;
import com.javacore.task.models.request.TrainerRequest;
import com.javacore.task.models.response.SignInResponse;
import com.javacore.task.models.response.SignUpResponse;
import com.javacore.task.models.request.SignInRequest;
import com.javacore.task.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/trainee/sign-up")
    public SignUpResponse traineeSignUp(@RequestBody TraineeRequest request){
        log.info("Endpoint called: POST /api/auth/trainee/sign-up");
        return authenticationService.traineeSignUp(request);
    }
    @PostMapping("/trainer/sign-up")
    public SignUpResponse trainerSignUp(@RequestBody TrainerRequest request){
        log.info("Endpoint called: POST /api/auth/trainer/sign-up");
        return authenticationService.trainerSignUp(request);
    }

    @PostMapping("/sign-in")
    public SignInResponse signIn(@RequestBody SignInRequest request){
        log.info("Endpoint called: POST /api/auth/sign-in");
        return authenticationService.signIn(request);
    }
}