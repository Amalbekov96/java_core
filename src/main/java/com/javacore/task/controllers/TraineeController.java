package com.javacore.task.controllers;

import com.javacore.task.models.TraineeModel;
import com.javacore.task.models.request.TraineeProfileUpdateResponse;
import com.javacore.task.models.request.TraineeTrainingsRequest;
import com.javacore.task.models.request.TraineeUpdateRequest;
import com.javacore.task.models.response.TraineeInfoResponse;
import com.javacore.task.models.response.TrainersListResponse;
import com.javacore.task.models.response.TraineeTrainingInfoResponse;
import com.javacore.task.services.TraineeService;
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
public class TraineeController {

    private final TraineeService traineeService;

    @GetMapping("/{traineeId}")
    @PreAuthorize("hasAuthority('TRAINEE')")
    public ResponseEntity<TraineeModel> getTraineeById(@PathVariable Long traineeId) {
        log.info("Endpoint called: GET /trainees/{}", traineeId);
        TraineeModel traineeModel = traineeService.getTraineeById(traineeId);
        log.info("Response: {}", traineeModel);
        return ResponseEntity.ok(traineeModel);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('TRAINEE')")
    public ResponseEntity<TraineeProfileUpdateResponse> updateTrainee(@Valid @RequestBody TraineeUpdateRequest request) {
        log.info("Endpoint called: PUT /trainees Request: {}", request);
        TraineeProfileUpdateResponse updatedTrainee = traineeService.updateTrainee(request);
        log.info("Response: {}", updatedTrainee);
        return ResponseEntity.ok(updatedTrainee);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTrainee(@RequestParam String username) {
        log.info("Endpoint called: DELETE /trainees?{}", username );
        traineeService.deleteTrainee(username);
        log.info("Response: deleted successfully");
        return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('TRAINEE')")
    public ResponseEntity<TraineeInfoResponse> getTraineeProfile(@RequestParam("q") String username) {
        log.info("Endpoint called: GET /trainees, Request: q={}", username);
        TraineeInfoResponse traineeGetByNameResponse = traineeService.findTraineeProfileByUsername(username);
        log.info("Response: {}", traineeGetByNameResponse);
        return new ResponseEntity<>(traineeGetByNameResponse, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<String> updateTraineeStatus(@RequestParam String username, @RequestParam("status") boolean status) {
        log.info("Endpoint called: PATCH /trainees?{}, Request: choice={}", username, status);
        String result = traineeService.updateTraineeStatus(status, username);
        log.info("Response: {}", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/not-assigned-trainers")
    @PreAuthorize("hasAuthority('TRAINEE')")
    public ResponseEntity<List<TrainersListResponse>> getNotAssignedTrainersForTrainee(@RequestParam String username) {
        log.info("Endpoint called: GET /trainees/notAssignedTrainers, Request: username={}", username);
        List<TrainersListResponse> responses = traineeService.getNotAssignedActiveTrainersListForTrainee(username);
        log.info("Response: {}", responses);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PutMapping("/update-trainers")
    public ResponseEntity<List<TrainersListResponse>> updateTraineeTrainersList(@RequestParam("username") String username, @RequestBody List<String> trainersUsernames) {
        log.info("Endpoint called: POST /trainees/updateList, Request: username={}, trainersUsernames={}", username, trainersUsernames);
        List<TrainersListResponse> responses = traineeService.updateTraineeTrainersList(username, trainersUsernames);
        log.info("Response: {}", responses);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/trainee-trainings")
    @PreAuthorize("hasAuthority('TRAINEE')")
    public ResponseEntity<List<TraineeTrainingInfoResponse>> getTraineeTrainingsList(@Valid @RequestBody TraineeTrainingsRequest request) {
        log.info("Endpoint called: POST /training/traineeTrainings, Request: {}", request);
        List<TraineeTrainingInfoResponse> responses = traineeService.getTraineeTrainingsByCriteria(request);
        log.info("Response: {}", responses);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}