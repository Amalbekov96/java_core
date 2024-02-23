package com.javacore.task.controllers;

import com.javacore.task.models.TraineeModel;
import com.javacore.task.models.response.TraineeProfileUpdateResponse;
import com.javacore.task.models.request.TraineeTrainingsRequest;
import com.javacore.task.models.request.TraineeUpdateRequest;
import com.javacore.task.models.response.TraineeInfoResponse;
import com.javacore.task.models.response.TrainersListResponse;
import com.javacore.task.models.response.TraineeTrainingInfoResponse;
import com.javacore.task.services.TraineeService;
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
@RequestMapping("/trainees")
@RequiredArgsConstructor
@Tag(name = "Trainee Controller", description = "Endpoints for Trainee operations")
public class TraineeController {

    private final TraineeService traineeService;

    @Operation(description = "Get Trainee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description= "Successfully retrieved trainee"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @GetMapping("/{traineeId}")
    @PreAuthorize("hasAuthority('TRAINEE')")
    public ResponseEntity<TraineeModel> getTraineeById(@PathVariable Long traineeId) {
        log.info("Endpoint called: GET /trainees/{}", traineeId);
        TraineeModel traineeModel = traineeService.getTraineeById(traineeId);
        log.info("Response: {}", traineeModel);
        return ResponseEntity.ok(traineeModel);
    }

    @Operation(description = "Update Trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description= "Successfully updated trainee"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @PutMapping
    @PreAuthorize("hasAuthority('TRAINEE')")
    public ResponseEntity<TraineeProfileUpdateResponse> updateTrainee(@Valid @RequestBody TraineeUpdateRequest request) {
        log.info("Endpoint called: PUT /trainees Request: {}", request);
        TraineeProfileUpdateResponse updatedTrainee = traineeService.updateTrainee(request);
        log.info("Response: {}", updatedTrainee);
        return ResponseEntity.ok(updatedTrainee);
    }

    @Operation(description = "Delete Trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description= "Successfully deleted trainee"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @DeleteMapping
    public ResponseEntity<String> deleteTrainee(@RequestParam String username) {
        log.info("Endpoint called: DELETE /trainees?username={}", username );
        traineeService.deleteTrainee(username);
        log.info("Response: deleted successfully");
        return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
    }

    @Operation(description = "Get Trainee Profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description= "Successfully retrieved trainee profile"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @GetMapping
    @PreAuthorize("hasAuthority('TRAINEE')")
    public ResponseEntity<TraineeInfoResponse> getTraineeProfile(@RequestParam("q") String username) {
        log.info("Endpoint called: GET /trainees?q={}", username);
        TraineeInfoResponse traineeGetByNameResponse = traineeService.findTraineeProfileByUsername(username);
        log.info("Response: {}", traineeGetByNameResponse);
        return new ResponseEntity<>(traineeGetByNameResponse, HttpStatus.OK);
    }

    @Operation(description = "Update Trainee Status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description= "Successfully updated trainee status"),
            @ApiResponse(responseCode = "404", description = "Trainee not found"),
            @ApiResponse(responseCode = "409", description = "Trainee is already in desired state")
    })
    @PatchMapping
    public ResponseEntity<String> updateTraineeStatus(@RequestParam String username, @RequestParam("status") boolean status) {
        log.info("Endpoint called: PATCH /trainees?username={}&choice={}", username, status);
        String result = traineeService.updateTraineeStatus(status, username);
        log.info("Response: {}", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(description = "Get Not Assigned Trainers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description= "Successfully retrieved not assigned trainers"),
            @ApiResponse(responseCode = "404", description = "Trainers not found")
    })
    @GetMapping("/not-assigned-trainers")
    @PreAuthorize("hasAuthority('TRAINEE')")
    public ResponseEntity<List<TrainersListResponse>> getNotAssignedTrainersForTrainee(@RequestParam String username) {
        log.info("Endpoint called: GET /not-assigned-trainers?username={}", username);
        List<TrainersListResponse> responses = traineeService.getNotAssignedActiveTrainersListForTrainee(username);
        log.info("Response: {}", responses);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(description = "Update Trainee Trainers List")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description= "Successfully updated trainee trainers list"),
            @ApiResponse(responseCode = "404", description = "Trainers not found")
    })
    @PutMapping("/update-trainers")
    public ResponseEntity<List<TrainersListResponse>> updateTraineeTrainersList(@RequestParam("username") String username, @RequestBody List<String> trainersUsernames) {
        log.info("Endpoint called: PUT /update-trainers?username={}, REQUEST: trainersUsernames={}", username, trainersUsernames);
        List<TrainersListResponse> responses = traineeService.updateTraineeTrainersList(username, trainersUsernames);
        log.info("Response: {}", responses);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(description = "Get Trainee Trainings List")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description= "Successfully retrieved trainee trainings list"),
            @ApiResponse(responseCode = "404", description = "Trainings not found")
    })
    @GetMapping("/trainee-trainings")
    @PreAuthorize("hasAnyAuthority('TRAINEE','TRAINER')")
    public ResponseEntity<List<TraineeTrainingInfoResponse>> getTraineeTrainingsList(@Valid @RequestBody TraineeTrainingsRequest request) {
        log.info("Endpoint called: GET trainee-trainings, Request: {}", request);
        List<TraineeTrainingInfoResponse> responses = traineeService.getTraineeTrainingsByCriteria(request);
        log.info("Response: {}", responses);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}