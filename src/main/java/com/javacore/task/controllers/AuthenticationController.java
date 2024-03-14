package com.javacore.task.controllers;

import com.javacore.task.models.request.PasswordUpdateRequest;
import com.javacore.task.models.request.TraineeRequest;
import com.javacore.task.models.request.TrainerRequest;
import com.javacore.task.models.response.SignInResponse;
import com.javacore.task.models.response.SignUpResponse;
import com.javacore.task.models.request.SignInRequest;
import com.javacore.task.services.AuthenticationService;
import io.micrometer.core.instrument.Counter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Timed
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Endpoints for Authentication operations")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final Counter counter;

    @Operation(summary = "Trainee Sign Up")
    @PostMapping("/trainee/sign-up")
    public SignUpResponse traineeSignUp(@Valid @RequestBody TraineeRequest request){
        log.info("Endpoint called: POST /api/v1/auth/trainee/sign-up");
        counter.increment();
        return authenticationService.traineeSignUp(request);
    }

    @Operation(summary = "Trainer Sign Up")
    @PostMapping("/trainer/sign-up")
    public SignUpResponse trainerSignUp(@Valid @RequestBody TrainerRequest request){
        log.info("Endpoint called: POST /api/v1/auth/trainer/sign-up");
        counter.increment();
        return authenticationService.trainerSignUp(request);
    }

    @Operation(summary = "Sign In")
    @PostMapping("/sign-in")
    public SignInResponse signIn(@Valid @RequestBody SignInRequest request){
        log.info("Endpoint called: POST /api/v1/auth/sign-in");
        counter.increment();
        return authenticationService.signIn(request);
    }

    @Operation(summary = "Update Password")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('TRAINER', 'TRAINEE')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully updated"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "400", description = "User can change only his password"),
            @ApiResponse(responseCode = "400", description = "Wrong password"),
    })
    public ResponseEntity<String> updateLogin(@Valid @RequestBody PasswordUpdateRequest request) {
        log.info("Endpoint called: POST /trainers?{}", request.getUsername());
        authenticationService.changePassword(request.getUsername(), request.getPassword(), request.getNewPassword());
        return new ResponseEntity<>("password successfully updated!", HttpStatus.OK);
    }

}