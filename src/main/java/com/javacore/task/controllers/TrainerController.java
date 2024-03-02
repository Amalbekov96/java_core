package com.javacore.task.controllers;

import com.javacore.task.models.TrainerModel;
import com.javacore.task.models.request.TrainerTrainingsRequest;
import com.javacore.task.models.request.TrainerUpdateRequest;
import com.javacore.task.models.response.TrainerInfoResponse;
import com.javacore.task.models.response.TrainerTrainingInfoResponse;
import com.javacore.task.models.response.TrainerUpdateResponse;
import com.javacore.task.services.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@PreAuthorize("hasAuthority('TRAINER')")
@RequestMapping("/trainers")
@RequiredArgsConstructor
@Tag(name = "Trainer API", description = "Endpoints for trainer operations")
public class TrainerController {

    private final TrainerService trainerService;

    @Operation(summary = "Get Trainer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Trainer by ID"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @GetMapping("/{trainerId}")
    public ResponseEntity<TrainerModel> getTrainerById(@PathVariable Long trainerId) {
        log.info("Endpoint called: GET /trainers/{}", trainerId);
        TrainerModel trainerModel = trainerService.getTrainerById(trainerId);
        log.info("Response: {}", trainerModel);
        return ResponseEntity.ok(trainerModel);
    }

    @Operation(summary = "Update Trainer Profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated Trainer profile"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @PutMapping
    public ResponseEntity<TrainerUpdateResponse> updateTrainer(@Valid @RequestBody TrainerUpdateRequest request) {
        log.info("Endpoint called: PUT /trainers Request: {}", request);
        TrainerUpdateResponse updatedTrainer = trainerService.updateTrainer(request);
        log.info("Response: {}", updatedTrainer);
        return ResponseEntity.ok(updatedTrainer);
    }

    @Operation(summary = "Get Trainer Profile by Username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Trainer profile by username"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @GetMapping
    public ResponseEntity<TrainerInfoResponse> getTrainerProfileByName(@RequestParam("trainerUsername") String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Trainer username cannot be null or empty");
        }
        log.info("Endpoint called: GET /trainer?q={}", username);
        TrainerInfoResponse response = trainerService.findTrainerProfileByUsername(username);
        log.info("Response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update Trainer Status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated Trainer status"),
            @ApiResponse(responseCode = "404", description = "Trainer not found"),
            @ApiResponse(responseCode = "409", description = "Trainer is already in desired state")
    })
    @PatchMapping
    public ResponseEntity<String> updateTrainerStatus(@RequestParam String username, @RequestParam("status") Boolean status) {
        if ((username == null || username.trim().isEmpty()) || status == null) {
            throw new IllegalArgumentException("Trainer username or status cannot be null or empty");
        }
        log.info("Endpoint called: PATCH /trainer?{}&choice={}", username, status);
        String result = trainerService.updateTrainerStatus(status, username);
        log.info("Response: {}", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Get Trainer Trainings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Trainer trainings"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @GetMapping("/trainings")
    public ResponseEntity<List<TrainerTrainingInfoResponse>> getTrainerTrainingsList(@Valid @RequestBody TrainerTrainingsRequest request) {
        log.info("Endpoint called: GET /trainer-trainings, Request: {}", request);
        List<TrainerTrainingInfoResponse> responses = trainerService.getTrainerTrainingsByCriteria(request);
        log.info("Response: {}", responses);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

}